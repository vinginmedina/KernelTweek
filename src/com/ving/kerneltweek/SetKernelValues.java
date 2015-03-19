package com.ving.kerneltweek;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class SetKernelValues implements Runnable {
	private KernelValue kv;

	public SetKernelValues(KernelValue kv) {
		this.kv = kv;
	}

	@Override
	public void run() {
		List<String> commands = kv.updateCommands();
		Shell.SU.run(commands);
	}

}
