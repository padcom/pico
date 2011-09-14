package com.aplaline.pico

interface Action {
	boolean configure(String[] args)
	void execute()
}
