package com.danosoftware.galaxyforce.flightpath.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents the base class for all Data Transfer Object paths.
 * <p>
 * Jackson annotations ensure the correct concrete implementation
 * is constructed on deserialization.
 * <p>
 * e.g. if the JSON path holds...
 * "type": "bezier"
 * ...then a Bezier path DTO is constructed using the other JSON fields.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BezierPathDTO.class, name = "bezier"),
        @JsonSubTypes.Type(value = LinearPathDTO.class, name = "linear"),
        @JsonSubTypes.Type(value = PausePathDTO.class, name = "pause"),
        @JsonSubTypes.Type(value = CircularPathDTO.class, name = "circular")
})
@JsonIgnoreProperties({"type"}) // prevents type enum also being serialized when creating JSON
public abstract class PathDTO {

    private final PathType pathType;

    PathDTO(final PathType pathType) {
        this.pathType = pathType;
    }

    /**
     * Return the path DTO's type.
     *
     * @return path type
     */
    public PathType getType() {
        return pathType;
    }
}
