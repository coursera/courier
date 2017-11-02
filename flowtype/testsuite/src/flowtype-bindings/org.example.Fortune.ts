import { FortuneTelling } from "./org.example.FortuneTelling";
import { DateTime } from "./org.example.common.DateTime";

/**
 * A fortune.
 */
export interface Fortune {
  /**
   * The fortune telling.
   */
  telling : FortuneTelling;
  
  createdAt : DateTime;
}
