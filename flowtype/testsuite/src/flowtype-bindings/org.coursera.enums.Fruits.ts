/**
 * An enum dedicated to the finest of the food groups.
 */
export type Fruits = "APPLE" | "BANANA" | "ORANGE" | "PINEAPPLE";
export module Fruits {
  /**
   * An Apple.
   */
  export const APPLE: Fruits = "APPLE";
  
  export const BANANA: Fruits = "BANANA";
  
  export const ORANGE: Fruits = "ORANGE";
  
  export const PINEAPPLE: Fruits = "PINEAPPLE";
  
  export const all: Array<Fruits> = ["APPLE", "BANANA", "ORANGE", "PINEAPPLE"];
}
