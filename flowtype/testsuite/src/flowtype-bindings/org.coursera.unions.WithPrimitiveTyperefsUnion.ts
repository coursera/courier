import { IntTyperef } from "./org.coursera.unions.IntTyperef";

export interface WithPrimitiveTyperefsUnion {
  
  union : WithPrimitiveTyperefsUnion.Union;
}
export module WithPrimitiveTyperefsUnion {
  
  export type Union = Union.IntTyperefMember;
  export module Union {
    export interface UnionMember {
      [key: string]: IntTyperef;
    }
    export interface IntTyperefMember extends UnionMember {
      "int": IntTyperef;
    }
    export function unpack(union: Union) {
      return {
        intTyperef: union["int"] as IntTyperef
      };
    }
  }
}
