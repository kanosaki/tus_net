package control;

public interface Unpacker {
	Message getType();
	Command unpack(byte[] data);
}
