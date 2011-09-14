package com.aplaline.pico.api

class Commit {
	String id
	String parentId
	String message
	long date = Calendar.instance.timeInMillis
	Tree tree
}
