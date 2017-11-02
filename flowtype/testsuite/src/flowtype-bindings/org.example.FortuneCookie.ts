/**
 * A fortune cookie.
 */
export interface FortuneCookie {
  /**
   * A fortune cookie message.
   */
  message : string;
  
  certainty ?: number;
  
  luckyNumbers : Array<number>;
}
