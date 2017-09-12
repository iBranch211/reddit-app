package org.baeldung.web.dto.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ScheduledPostUpdateCommandDto extends ScheduledPostCommandDto {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String uuid;

    public ScheduledPostUpdateCommandDto() {
        super();
    }

    //

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getSubmissionDateConverted(final String timezone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        return dateFormat.parse(getDate());
    }

}
