package at.tomtasche.joppfm.database;

import java.util.LinkedList;
import java.util.List;

/**
 * A prototype of a more dynamic wrapper of the database
 * 
 * @author tom
 *
 */
public class ExperimentalDatabaseHelper {

	private static final List<Variable> variables = new LinkedList<Variable>();

	public static void addVariable(String name, String type) {
		variables.add(new Variable(name, type));
	}

	public static String buildCreateStatement(String tableName) {
		return "CREATE ...";
	}

	public static int getColumnIndex(String name) {
		for (int i = 0; i < variables.size(); i++) {
			Variable variable = variables.get(i);
			if (variable.getName().equals(name))
				return i;
		}

		return -1;
	}

	private static class Variable {

		private String name;
		// TODO: change type of "type" (hah!) to Class or something, in order to
		// represent the actual type
		private String type;
		private String[] options;

		public Variable(String name, String type) {
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
