package com.comp2042.logic.movement;

/**
 * Defines the source of a game event, either from user input or an automated thread.
 */
public enum EventSource {
    /** Indicates the event was triggered by user input. */
    USER, /** Indicates the event was triggered by an automated process (e.g., game thread). */
    THREAD
}
