package com.salmac.agent.utils;

import com.salmac.agent.entity.ScriptType;
import com.salmac.agent.utils.OSNAME;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Utils {

	public static HashMap<String, LocalDateTime> serverLastHeartbeatMap = new HashMap<>();

	public static void replaceAll(StringBuilder builder, String from, String to) {
	    int index = builder.indexOf(from);
	    while (index != -1) {
	        builder.replace(index, index + from.length(), to);
	        index += to.length(); // Move to the end of the replacement
	        index = builder.indexOf(from, index);
	    }
	}

	public static boolean isEmptyString(String string) {
		return string == null || string.isEmpty();
	}

	public static ScriptType assumeScriptType(String fileName){
		if(fileName.endsWith(".sh")){
			return ScriptType.Shell;
		} else if (fileName.endsWith(".py")) {
			return ScriptType.Python;
		} else if (fileName.endsWith(".bat")) {
			return ScriptType.Batch;
		} else if (fileName.endsWith(".cmd")) {
			return ScriptType.Command;
		}
		//Handle default
		if (OSNAME.isWindows()) {
			return ScriptType.Batch;//By default Batch for Windows
		} else {
			return ScriptType.Shell;// By default, Script type is Shell for any other OS
		}
	}
}
