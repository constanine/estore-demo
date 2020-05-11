package com.bokesoft.ecomm.estore.mid.connection.struc;

public class DataType {
	// 之所以不直接引用JDBC的Types的值，是因为平台支持的数据类型与数据库中类型不是一对一的关系，这里的类型做了一定程度的抽象。
		// 使用大于一千的整数值是避免与java.sql,Types中的值发生重复，发生误用。

		/** 32位整型 */
		public static final int INT = 1001;
		public static final String STR_INT = "Integer";

		/** 字符串类型 */
		public static final int STRING = 1002;
		public static final String STR_STRING = "Varchar";

		/** 日期类型 */
		public static final int DATE = 1003;
		public static final String STR_DATE = "Date";

		/** 日期时间类型 */
		public static final int DATETIME = 1004;
		public static final String STR_DATETIME = "DateTime";

		/** 数值类型 */
		public static final int NUMERIC = 1005;
		public static final String STR_NUMERIC = "Numeric";

		/** 双精度浮点数类型 */
		public static final int DOUBLE = 1006;
		public static final String STR_DOUBLE = "Double";

		/** 浮点数类型 */
		public static final int FLOAT = 1007;
		public static final String STR_FLOAT = "Float";

		/** 二进制类型 */
		public static final int BINARY = 1008;
		public static final String STR_BINARY = "Binary";

		/** 布尔类型 */
		public static final int BOOLEAN = 1009;
		public static final String STR_BOOLEAN = "Boolean";

		/** 64位整型 */
		public static final int LONG = 1010;
		public static final String STR_LONG = "Long";

		/** 文本类型 */
		public static final int TEXT = 1011;
		public static final String STR_TEXT = "Text";
		
		/** 定长字符串类型 */
		public static final int FIXED_STRING = 1012;
		public static final String STR_FIXED_STRING = "Char";

		/**
		 * 从字符串解析数据表示的类型
		 * 
		 * @param type
		 *            字符串
		 * @return 如果是有效的字符串,返回相应的数据类型,否则返回-1
		 */
		public static int parse(String type) {
			if (type.equalsIgnoreCase(STR_INT)) {
				return INT;
			} else if (type.equalsIgnoreCase(STR_STRING)) {
				return STRING;
			} else if (type.equalsIgnoreCase(STR_NUMERIC)) {
				return NUMERIC;
			} else if (type.equalsIgnoreCase(STR_DATE)) {
				return DATE;
			} else if (type.equalsIgnoreCase(STR_DATETIME)) {
				return DATETIME;
			} else if (type.equalsIgnoreCase(STR_DOUBLE)) {
				return DOUBLE;
			} else if (type.equalsIgnoreCase(STR_BINARY)) {
				return BINARY;
			} else if (type.equalsIgnoreCase(STR_FLOAT)) {
				return FLOAT;
			} else if (type.equalsIgnoreCase(STR_BOOLEAN)) {
				return BOOLEAN;
			} else if (type.equalsIgnoreCase(STR_LONG)) {
				return LONG;
			}else if (type.equalsIgnoreCase(STR_TEXT)) {
				return TEXT;
			}else if (type.equalsIgnoreCase(STR_FIXED_STRING)) {
				return FIXED_STRING;
			}
			return -1;
		}

		/**
		 * 根据数据类型,转换成字符串
		 * 
		 * @param type
		 *            数据类型
		 * @return 如果是有效的类型,返回相应的字符串表示,否则返回null
		 */
		public static String toString(int type) {
			switch (type) {
			case INT:
				return STR_INT;
			case STRING:
				return STR_STRING;
			case NUMERIC:
				return STR_NUMERIC;
			case DATE:
				return STR_DATE;
			case DATETIME:
				return STR_DATETIME;
			case DOUBLE:
				return STR_DOUBLE;
			case FLOAT:
				return STR_FLOAT;
			case BINARY:
				return STR_BINARY;
			case BOOLEAN:
				return STR_BOOLEAN;
			case LONG:
				return STR_LONG;
			case TEXT:
				return STR_TEXT;
			case FIXED_STRING:
				return STR_FIXED_STRING;
			case -1:
				return "-1";
			}
			return "";
		}
}
