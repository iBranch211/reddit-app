package org.baeldung.service;

public enum TokenState {
    VALID, INVALID, EXPIRED;

    @Override
    public String toString() {
        switch (this) {
        case VALID:
            return "Valid Token";
        case INVALID:
            return "Invalid Token";
        case EXPIRED:
            return "Token Expired";
        }
        return null;
    }
}
