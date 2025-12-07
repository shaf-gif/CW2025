package com.comp2042.logic.movement;

/**
 * Represents a movement event in the game, encapsulating the type of event
 * and its source (user input or automated process).
 */
public final class MoveEvent {
    /**
     * The type of movement event.
     */
    private final EventType eventType;
    /**
     * The source of the movement event.
     */
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent with the specified event type and source.
     *
     * @param eventType   The type of the movement event.
     * @param eventSource The source that triggered the event.
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Returns the type of movement event.
     *
     * @return The {@code EventType} of this event.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source of the movement event.
     *
     * @return The {@code EventSource} of this event.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
