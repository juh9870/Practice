package cf.juh9870;


import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class Console {

	private static Terminal terminal;
	private static LineReader reader;


	public static void start() {
		try {
			terminal = TerminalBuilder
					.builder()
					.system(true)
					.build();
			reader = LineReaderBuilder.builder().build();
		} catch (IOException e) {
			System.out.print("Error initialising terminal");
			System.exit(-1);
		}
	}

	public static void print(Object data) {
		terminal.writer().print(data);
		terminal.flush();
	}

	public static void printLine(Object data) {
		terminal.writer().println(data);
		terminal.flush();
	}

	public static void clear() {
		terminal.puts(InfoCmp.Capability.clear_screen);
	}

	public static char getKeyPress() {
		String text = readLine();
		if (text.length() == 0) return '\r';
		return text.charAt(0);
	}

	public static String readLine() {
		try {
			String text = reader.readLine();
			return text;
		} catch (EndOfFileException e) {
			System.exit(0);
			return "";
		}
	}

	public static class Color {
		public static String RED = "\u001b[31m";
		public static String GREEN = "\u001b[32m";
		public static String RESET = "\u001b[0m";
	}
}
