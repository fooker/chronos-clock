package chronos;

public class Record {
  private long timestamp;
  private double movement;

  public Record(long timestamp, double movement) {
    this.timestamp = timestamp;
    this.movement = movement;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public double getMovement() {
    return this.movement;
  }
}
