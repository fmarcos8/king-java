package engine.utils;

import java.util.HashMap;
import java.util.Map;

public class Cheats {
	private static String[] cheats_list = new String[] {
		"flymode",
		"teste"
	};
	private static HashMap<String, Boolean> listCheats = new HashMap<String, Boolean>();
	private static String TryCheat = "";
	
	public static void InitCheats() {
		for (String s : cheats_list) {
			listCheats.put(s, false);
		}
	}
	
	public static boolean CheatActivated(String cheat) {
		if(listCheats.containsKey(cheat)) {
			return listCheats.get(cheat);
		}
		return false;
	}
	
	public static void TestCheat(char c) {
		TryCheat += c;
		boolean has = false;
		for (Map.Entry<String, Boolean> pair : listCheats.entrySet()) {
			if(pair.getKey().contains(TryCheat)) {
				has = true;
			}
		}
		
		if(!has) {
			TryCheat = "";
		} else if(listCheats.containsKey(TryCheat)) {
			listCheats.put(TryCheat, !listCheats.get(TryCheat));
			TryCheat = "";
		}
	}
	
	public static String GetTryCheat() {
		return TryCheat;
	}
}
