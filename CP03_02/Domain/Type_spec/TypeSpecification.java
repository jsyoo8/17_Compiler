package Domain.Type_spec;

import Domain.MiniCNode;

public class TypeSpecification extends MiniCNode {
	final Type type;

	public enum Type {
		VOID, INT
	}

	public TypeSpecification(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type.toString().toLowerCase();
	}
}
