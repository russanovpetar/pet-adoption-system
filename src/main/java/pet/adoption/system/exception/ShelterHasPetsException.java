package pet.adoption.system.exception;

public class ShelterHasPetsException extends RuntimeException {
  public ShelterHasPetsException() {
    super("Cannot delete shelter that has pets. Reassign or remove pets first.");
  }
}
