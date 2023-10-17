package com.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator
 * </p>
 *
 * @author 星哥
 * @since 2023-5-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * auto increment id
     */
    //@TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * host name
     */
    //@TableField("HOST_NAME")
    private String hostName;

    /**
     * port
     */
    //@TableField("PORT")
    private String port;

    /**
     * node type: ACTUAL or CONTAINER
     */
    //@TableField("TYPE")
    private Integer type;

    /**
     * launch date
     */
    //@TableField("LAUNCH_DATE")
    private LocalDate launchDate;

    /**
     * modified time
     */
    //@TableField("MODIFIED")
    private LocalDateTime modified;

    /**
     * created time
     */
    //@TableField("CREATED")
    private LocalDateTime created;


}