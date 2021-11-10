package com.bootcamp.demo.model.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractModel implements Serializable {

    private UUID uuid;

    protected AbstractModel() { this.uuid = UUID.randomUUID(); }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModel that = (AbstractModel) o;
        return Objects.equals(this.getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUuid());
    }
}
