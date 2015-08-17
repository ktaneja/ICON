package edu.ncsu.csc.ase.dristi.knowledge;

/**
 * 
 * @author Rahul Pandita
 * Created: Dec 24, 2013 3:30:36 AM
 */
public class JavaAPIKnowledge 
{
	public static void addKnowledge() 
	{
		addKnowledgeBufferedInputStream();
		addKnowledgeBufferedOutputStream();
		addKnowledgeBufferedReader();
		addKnowledgeBufferedWriter();
		addKnowledgeByteArrayInputStream();
		addKnowledgeByteArrayOutputStream();
		addKnowledgeCharArrayReader();
		addKnowledgeCharArrayWriter();
		addKnowledgeCharConversionException();
		addKnowledgeConsole();
		addKnowledgeDataInputStream();
		addKnowledgeDataOutputStream();
		addKnowledgeEOFException();
		addKnowledgeFile();
		addKnowledgeFileDescriptor();
		addKnowledgeFileInputStream();
		addKnowledgeFileNotFoundException();
		addKnowledgeFileOutputStream();
		addKnowledgeFilePermission();
		addKnowledgeFilePermissionCollection();
		addKnowledgeFileReader();
		addKnowledgeFileWriter();
		addKnowledgeFilterInputStream();
		addKnowledgeFilterOutputStream();
		addKnowledgeInputStreamReader();
		addKnowledgeInterruptedIOException();
		addKnowledgeInvalidClassException();
		addKnowledgeInvalidObjectException();
		addKnowledgeIOError();
		addKnowledgeIOException();
		addKnowledgeLineNumberInputStream();
		addKnowledgeLineNumberReader();
		addKnowledgeNotActiveException();
		addKnowledgeNotSerializableException();
		addKnowledgeObjectInputStream();
		addKnowledgeObjectOutputStream();
		addKnowledgeObjectStreamClass();
		addKnowledgeObjectStreamField();
		addKnowledgeOptionalDataException();
		addKnowledgeOutputStreamWriter();
		addKnowledgePipedInputStream();
		addKnowledgePipedOutputStream();
		addKnowledgePipedReader();
		addKnowledgePipedWriter();
		addKnowledgePrintStream();
		addKnowledgePrintWriter();
		addKnowledgePushbackInputStream();
		addKnowledgePushbackReader();
		addKnowledgeRandomAccessFile();
		addKnowledgeSequenceInputStream();
		addKnowledgeSerialCallbackContext();
		addKnowledgeSerializablePermission();
		addKnowledgeStreamCorruptedException();
		addKnowledgeStreamTokenizer();
		addKnowledgeStringBufferInputStream();
		addKnowledgeStringReader();
		addKnowledgeStringWriter();
		addKnowledgeSyncFailedException();
		addKnowledgeUnsupportedEncodingException();
		addKnowledgeUTFDataFormatException();
		addKnowledgeWriteAbortedException();

	}

	private static void addKnowledgeBufferedInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("BufferedInputStream");
		ka.setSynonyms("BufferedInputStream", "InputStream", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "available", "read", "skip",
				"support");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBufferedOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("BufferedOutputStream");
		ka.setSynonyms("BufferedOutputStream", "OutputStream", "Stream");
		ka.setActions("write", "flush");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBufferedReader() {
		KnowledgeAtom ka = new KnowledgeAtom("BufferedReader");
		ka.setSynonyms("BufferedReader", "reader", "mark", "stream");
		ka.setActions("close", "readLine", "mark", "reset", "read", "skip",
				"support", "ready");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeBufferedWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("BufferedWriter");
		ka.setSynonyms("BufferedWriter", "Writer", "Stream");
		ka.setActions("write", "close", "flush", "newLine");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeByteArrayInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("ByteArrayInputStream");
		ka.setSynonyms("ByteArrayInputStream", "ArrayInputStream",
				"InputStream", "stream", "mark");
		ka.setActions("close", "mark", "reset", "available", "read", "skip",
				"support");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeByteArrayOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("ByteArrayOutputStream");
		ka.setSynonyms("ByteArrayOutputStream", "ArrayOutputStream",
				"OutputStream", "Stream");
		ka.setActions("size", "write", "close", "reset");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeCharArrayReader() {
		KnowledgeAtom ka = new KnowledgeAtom("CharArrayReader");
		ka.setSynonyms("CharArrayReader", "ArrayReader", "Reader", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "read", "skip", "support",
				"ready");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeCharArrayWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("CharArrayWriter");
		ka.setSynonyms("CharArrayWriter", "Writer", "Stream", "ArrayWriter");
		ka.setActions("append", "size", "write", "close", "flush", "reset");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeCharConversionException() {
		KnowledgeAtom ka = new KnowledgeAtom("CharConversionException");
		ka.setSynonyms("CharConversionException", "ConversionException",
				"Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeConsole() {
		KnowledgeAtom ka = new KnowledgeAtom("Console");
		ka.setSynonyms("Console", "Line", "password", "writer", "reader");
		ka.setActions("format", "access", "flush", "read", "print", "read");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeDataInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("DataInputStream");
		ka.setSynonyms("DataInputStream", "InputStream", "Stream");
		ka.setActions("read", "skip", "close");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeDataOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("DataOutputStream");
		ka.setSynonyms("DataOutputStream", "OutputStream", "Stream");
		ka.setActions("size", "write", "flush", "close");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeEOFException() {
		KnowledgeAtom ka = new KnowledgeAtom("EOFException");
		ka.setSynonyms("EOFException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFile() {
		KnowledgeAtom ka = new KnowledgeAtom("File");
		ka.setSynonyms("File");
		ka.setActions(  "compareTo", "getName", "length",
				"getCanonicalPath", "getParent", "isAbsolute", "setReadOnly",
				"list", "delete", "exists", "getPath", "isInvalid",
				"canExecute", "canRead", "canWrite", "createNewFile",
				"createTempFile", "deleteOnExit", "getAbsoluteFile",
				"getAbsolutePath", "getCanonicalFile", "getFreeSpace",
				"getParentFile", "getTotalSpace", "getUsableSpace",
				"isDirectory", "isFile", "isHidden", "lastModified",
				"listFiles", "listRoots", "mkdir", "mkdirs", "renameTo",
				"setExecutable", "setLastModified", "setReadable",
				"setWritable", "toPath", "toURI", "toURL");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileDescriptor() {
		KnowledgeAtom ka = new KnowledgeAtom("FileDescriptor");
		ka.setSynonyms("FileDescriptor", "Descriptor");
		ka.setActions("access", "sync", "valid");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("FileInputStream");
		ka.setSynonyms("FileInputStream", "InputStream", "stream", "Channel",
				"FD");
		ka.setActions("finalize", "close", "available", "get", "read", "skip");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileNotFoundException() {
		KnowledgeAtom ka = new KnowledgeAtom("FileNotFoundException");
		ka.setSynonyms("FileNotFoundException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("FileOutputStream");
		ka.setSynonyms("FileOutputStream", "OutputStream", "Stream", "Channel",
				"FD");
		ka.setActions("finalize", "write", "close", "get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFilePermission() {
		KnowledgeAtom ka = new KnowledgeAtom("FilePermission");
		ka.setSynonyms("FilePermission", "Permission");
		ka.setActions(  "access", "get", "implies");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFilePermissionCollection() {
		KnowledgeAtom ka = new KnowledgeAtom("FilePermissionCollection");
		ka.setSynonyms("FilePermissionCollection", "PermissionCollection");
		ka.setActions("add", "elements", "implies");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileReader() {
		KnowledgeAtom ka = new KnowledgeAtom("FileReader");
		ka.setSynonyms("FileReader", "Reader", "Stream");
		ka.setActions("read");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFileWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("FileWriter");
		ka.setSynonyms("FileWriter", "Writer", "Stream");
		ka.setActions("Write");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFilterInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("FilterInputStream");
		ka.setSynonyms("FilterInputStream", "InputStream", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "available", "read", "skip",
				"support");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeFilterOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("FilterOutputStream");
		ka.setSynonyms("FilterOutputStream", "OutputStream", "Stream");
		ka.setActions("write", "close", "flush");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeInputStreamReader() {
		KnowledgeAtom ka = new KnowledgeAtom("InputStreamReader");
		ka.setSynonyms("InputStreamReader", "StreamReader", "Reader", "Stream");
		ka.setActions("close", "read", "getEncoding", "ready");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeInterruptedIOException() {
		KnowledgeAtom ka = new KnowledgeAtom("InterruptedIOException");
		ka.setSynonyms("InterruptedIOException", "IOException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeInvalidClassException() {
		KnowledgeAtom ka = new KnowledgeAtom("InvalidClassException");
		ka.setSynonyms("InvalidClassException", "ClassException", "Exception",
				"Message");
		ka.setActions("get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeInvalidObjectException() {
		KnowledgeAtom ka = new KnowledgeAtom("InvalidObjectException");
		ka.setSynonyms("InvalidObjectException", "ObjectException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeIOError() {
		KnowledgeAtom ka = new KnowledgeAtom("IOError");
		ka.setSynonyms("IOError", "Error");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeIOException() {
		KnowledgeAtom ka = new KnowledgeAtom("IOException");
		ka.setSynonyms("IOException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeLineNumberInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("LineNumberInputStream");
		ka.setSynonyms("LineNumberInputStream", "NumberInputStream",
				"InputStream", "Stream", "Line Number", "LineNumber");
		ka.setActions("get", "mark", "reset", "available", "read", "skip",
				"set");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeLineNumberReader() {
		KnowledgeAtom ka = new KnowledgeAtom("LineNumberReader");
		ka.setSynonyms("LineNumberReader", "NumberReader", "Reader", "Stream",
				"LineNumber", "Line Number", "Line");
		ka.setActions("get", "mark", "reset", "read", "skip", "set");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeNotActiveException() {
		KnowledgeAtom ka = new KnowledgeAtom("NotActiveException");
		ka.setSynonyms("NotActiveException", "ActiveException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeNotSerializableException() {
		KnowledgeAtom ka = new KnowledgeAtom("NotSerializableException");
		ka.setSynonyms("NotSerializableException", "SerializableException",
				"Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeObjectInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectInputStream");
		ka.setSynonyms("ObjectInputStream", "InputStream", "Stream", "Object",
				"Line");
		ka.setActions("access", "resolveClass", "read", "close", "read",
				"available", "skip", "enable", "resolve");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeObjectOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectOutputStream");
		ka.setSynonyms("ObjectOutputStream", "OutputStream", "Stream",
				"Object", "Fields");
		ka.setActions("access", "write", "close", "flush", "put", "reset",
				"annotateClass", "annotate");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeObjectStreamClass() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectStreamClass");
		ka.setSynonyms("ObjectStreamClass", "StreamClass", "Field", "Queue");
		ka.setActions("get", "access", "process", "lookup");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeObjectStreamField() {
		KnowledgeAtom ka = new KnowledgeAtom("ObjectStreamField");
		ka.setSynonyms("ObjectStreamField", "StreamField", "Field");
		ka.setActions("isPrimitive", "compareTo", "getName", "getType",
				"getOffset", "getTypeCode", "getTypeString", "isUnshared",
				"setOffset");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeOptionalDataException() {
		KnowledgeAtom ka = new KnowledgeAtom("OptionalDataException");
		ka.setSynonyms("OptionalDataException", "DataException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeOutputStreamWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("OutputStreamWriter");
		ka.setSynonyms("OutputStreamWriter", "StreamWriter", "Writer", "Stream",
				"Encoding");
		ka.setActions("write", "close", "flush", "get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePipedInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("PipedInputStream");
		ka.setSynonyms("PipedInputStream", "InputStream", "Stream", "Pipe");
		ka.setActions("close", "available", "read", "connect", "receive");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePipedOutputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("PipedOutputStream");
		ka.setSynonyms("PipedOutputStream", "OutputStream", "Stream", "Pipe");
		ka.setActions("write", "close", "flush", "connect");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePipedReader() {
		KnowledgeAtom ka = new KnowledgeAtom("PipedReader");
		ka.setSynonyms("PipedReader", "Reader", "Stream", "Pipe");
		ka.setActions("close", "read", "ready", "connect", "receive");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePipedWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("PipedWriter");
		ka.setSynonyms("PipedWriter", "Writer", "Stream", "Pipe");
		ka.setActions("write", "close", "flush", "connect");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePrintStream() {
		KnowledgeAtom ka = new KnowledgeAtom("PrintStream");
		ka.setSynonyms("PrintStream", "Stream", "Error");
		ka.setActions("print", "append", "format", "write", "close", "flush",
				"check", "clear", "set");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePrintWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("PrintWriter");
		ka.setSynonyms("PrintWriter", "Writer", "Stream", "Error");
		ka.setActions("print", "append", "format", "write", "close", "flush",
				"check", "clear", "set");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePushbackInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("PushbackInputStream");
		ka.setSynonyms("PushbackInputStream", "InputStream", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "available", "read", "skip",
				"support", "unread");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgePushbackReader() {
		KnowledgeAtom ka = new KnowledgeAtom("PushbackReader");
		ka.setSynonyms("PushbackReader", "Reader", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "read", "skip", "support",
				"ready", "unread");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeRandomAccessFile() {
		KnowledgeAtom ka = new KnowledgeAtom("RandomAccessFile");
		ka.setSynonyms("RandomAccessFile", "AccessFile", "File");
		ka.setActions("length", "write", "close", "read", "set", "get", "skip",
				"seek");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeSequenceInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("SequenceInputStream");
		ka.setSynonyms("SequenceInputStream", "InputStream", "Stream");
		ka.setActions("close", "available", "read", "next");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeSerialCallbackContext() {
		KnowledgeAtom ka = new KnowledgeAtom("SerialCallbackContext");
		ka.setSynonyms("SerialCallbackContext", "CallbackContext", "Context",
				"Desc", "Description", "Obj", "Object", "Used");
		ka.setActions("get", "set");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeSerializablePermission() {
		KnowledgeAtom ka = new KnowledgeAtom("SerializablePermission");
		ka.setSynonyms("SerializablePermission", "Permission");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeStreamCorruptedException() {
		KnowledgeAtom ka = new KnowledgeAtom("StreamCorruptedException");
		ka.setSynonyms("StreamCorruptedException", "CorruptedException",
				"Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeStreamTokenizer() {
		KnowledgeAtom ka = new KnowledgeAtom("StreamTokenizer");
		ka.setSynonyms("StreamTokenizer", "Tokenizer", "Token");
		ka.setActions("next", "commentChar", "eolIsSignificant", "lineno",
				"lowerCaseMode", "ordinaryChar", "ordinaryChars",
				"parseNumbers", "quoteChar", "resetSyntax",
				"slashSlashComments", "slashStarComments", "whitespaceChars",
				"wordChars", "pushBack");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeStringBufferInputStream() {
		KnowledgeAtom ka = new KnowledgeAtom("StringBufferInputStream");
		ka.setSynonyms("StringBufferInputStream", "BufferInputStream",
				"InputStream", "Stream");
		ka.setActions("reset", "available", "read", "skip");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeStringReader() {
		KnowledgeAtom ka = new KnowledgeAtom("StringReader");
		ka.setSynonyms("StringReader", "Reader", "Stream", "mark");
		ka.setActions("close", "mark", "reset", "read", "skip", "support",
				"ready");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeStringWriter() {
		KnowledgeAtom ka = new KnowledgeAtom("StringWriter");
		ka.setSynonyms("StringWriter", "Writer", "Stream", "Buffer");
		ka.setActions("append", "write", "close", "flush", "get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeSyncFailedException() {
		KnowledgeAtom ka = new KnowledgeAtom("SyncFailedException");
		ka.setSynonyms("SyncFailedException", "FailedException", "Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeUnsupportedEncodingException() {
		KnowledgeAtom ka = new KnowledgeAtom("UnsupportedEncodingException");
		ka.setSynonyms("UnsupportedEncodingException", "EncodingException",
				"Exception");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeUTFDataFormatException() {
		KnowledgeAtom ka = new KnowledgeAtom("UTFDataFormatException");
		ka.setSynonyms("UTFDataFormatException", "Exception",
				"DataFormatException", "FormatException");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}

	private static void addKnowledgeWriteAbortedException() {
		KnowledgeAtom ka = new KnowledgeAtom("WriteAbortedException");
		ka.setSynonyms("WriteAbortedException", "Exception", "Cause", "Message");
		ka.setActions("get");
		Knowledge.getInstance().addKnowledgeAtom(ka);
	}
}
