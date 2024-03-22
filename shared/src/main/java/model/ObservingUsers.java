package model;

import java.util.Collection;
import java.util.HashSet;

public record ObservingUsers (Collection<String> observers) {
//    public ObservingUsers() {
//        this(observers, new HashSet<>());
//    }
//    public ObservingUsers(Collection<String> observers) {
//        this.observers = observers;
//    }
//    Collection<String> observers = new HashSet<>();
}
