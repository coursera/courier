/**
 * Magic eight ball answers.
 */
export type MagicEightBallAnswer = "IT_IS_CERTAIN" | "ASK_AGAIN_LATER" | "OUTLOOK_NOT_SO_GOOD";
export module MagicEightBallAnswer {
  
  export const IT_IS_CERTAIN: MagicEightBallAnswer = "IT_IS_CERTAIN";
  /**
   * Where later is at least 10 ms from now.
   */
  export const ASK_AGAIN_LATER: MagicEightBallAnswer = "ASK_AGAIN_LATER";
  
  export const OUTLOOK_NOT_SO_GOOD: MagicEightBallAnswer = "OUTLOOK_NOT_SO_GOOD";
  
  export const all: Array<MagicEightBallAnswer> = ["IT_IS_CERTAIN", "ASK_AGAIN_LATER", "OUTLOOK_NOT_SO_GOOD"];
}
