import java.util.zip.*
import java.security.*
import groovy.io.*
import java.text.*

class Commit {
    String id
    String parentId
    String message
    long date = Calendar.instance.timeInMillis
    Tree tree
}

class Tree {
    String id
    List<Entry> entries = new ArrayList<Entry>()
}

class Entry {
    String id
    String path;
    byte[] blob;
}

String makeRelative(String path) {
    new File(".").toURI().relativize(new File(path).toURI()).getPath();
}

String sha1(byte[] input) {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    new BigInteger(1, md.digest(input)).toString(16).padLeft( 40, '0' ) 
}

if (args.size() > 0 && args[0] == "log") {
    def head = new File(".pico/HEAD").readLines()[0]
    def commit = new File(".pico/objects/" + head)
    while (commit.exists()) {
        commit.withReader {
            parent = it.readLine()
            println "id       : " + head
            println "parent   : " + parent
            println "timestamp: " + it.readLine()
            println "tree     : " + it.readLine()
            println "comment  : " + it.readLines().join("\n") + "\n"
            
            commit = new File(".pico/objects/" + parent)
            head = parent
        }
    }
    return
} 

else if (args.size() > 0 && args[0] == "co") {
    def ant = new AntBuilder()
    
    def commit = new Commit()
    new File(".pico/HEAD").withReader { commit.id = it.readLine() }
    new File(".pico/objects/" + commit.id).withReader {
        commit.parentId = it.readLine()
        commit.date = Long.parseLong(it.readLine())
        commit.tree = new Tree()
        commit.tree.id = it.readLine()
    }
    
    new File(".pico/objects/" + commit.tree.id).eachLine { line ->        
        def entry = new Entry(id: line.substring(0, 40), path: ".pico-test/" + line.substring(41))
        entry.blob = new File(".pico/objects/" + entry.id).bytes
        commit.tree.entries.add(entry)
    }
    
    new File(".pico-test/").mkdir()
    
    commit.tree.entries.each { entry ->
        println entry.path
        def output = new File(entry.path)
        ant.mkdir(dir: output.parent)
        output.bytes = new GZIPInputStream(new ByteArrayInputStream(entry.blob)).bytes
    }
    
    return
} 

else if (args.size() > 1 && args[0] == "ci") {
    def tree = new Tree()
    def treeId = ""
    
    new File(".").eachFileRecurse(FileType.FILES) { file ->
        if (file.absolutePath.contains(".pico")) return
    
        println makeRelative(file.absolutePath)
        def entry = new Entry(path: makeRelative(file.absolutePath))
    
        def temp = new ByteArrayOutputStream()
        def zip  = new GZIPOutputStream(temp)
        zip.write(file.bytes)
        zip.finish()
        entry.blob = temp.toByteArray()
        entry.id = sha1(entry.blob)
        println entry.id
        treeId += entry.id
        
        tree.entries.add(entry)
    }
    
    tree.id = sha1(treeId.bytes)
    
    def commit = new Commit(tree: tree)
    commit.message = args[1]
    
    def head = new File(".pico/HEAD")
    
    if (head.exists()) {
        commit.parentId = head.withReader { it.readLine() }
    }
    
    commit.id = sha1((commit.parentId + commit.message + tree.id).bytes)
    
    new File(".pico").mkdir()
    new File(".pico/objects").mkdir()
    
    new File(".pico/objects/" + commit.id).withWriter { 
        it.println commit.parentId
        it.println commit.date
        it.println commit.tree.id
        it.println commit.message
    }
    
    new File(".pico/objects/" + commit.tree.id).withWriter { w ->
        commit.tree.entries.each { entry ->
            w.println("${entry.id}=${entry.path}")
        }
    }
    
    commit.tree.entries.each { entry ->
        new File(".pico/objects/" + entry.id).bytes = entry.blob
    }
    
    
    head.withWriter { w -> w.println commit.id }
}
