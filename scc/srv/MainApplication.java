package scc.srv;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MainApplication extends Application
{
	private final Set<Object> singletons = new HashSet<>();
	private final Set<Class<?>> resources = new HashSet<>();

	public MainApplication() {
		resources.add(scc.srv.api.ControlResource.class);
		resources.add(scc.srv.api.MediaResource.class);
		resources.add(scc.srv.api.CalendarResource.class);
		resources.add(scc.srv.api.EntityResource.class);
		resources.add(scc.srv.api.ForumResource.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return resources;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
