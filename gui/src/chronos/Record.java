package chronos;

import java.util.Date;

public class Record {
  private Date timestamp;
  private int movement;

  public Record(Date timestamp, int movement) {
    this.timestamp = timestamp;
    this.movement = movement;
  }

  public Date getTimestamp() {
    return this.timestamp;
  }

  public int getMovement() {
    return this.movement;
  }
}
