package github.iri.tridot.rhino;

public class BeanProperty {
	MemberBox getter;
	MemberBox setter;
	NativeJavaMethod setters;

	BeanProperty(MemberBox getter, MemberBox setter, NativeJavaMethod setters) {
		this.getter = getter;
		this.setter = setter;
		this.setters = setters;
	}
}
