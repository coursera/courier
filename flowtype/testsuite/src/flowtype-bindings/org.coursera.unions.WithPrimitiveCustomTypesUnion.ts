import { IntCustomType } from "./org.coursera.unions.IntCustomType";

export interface WithPrimitiveCustomTypesUnion {
  
  union : WithPrimitiveCustomTypesUnion.Union;
}
export module WithPrimitiveCustomTypesUnion {
  
  export type Union = Union.IntCustomTypeMember;
  export module Union {
    export interface UnionMember {
      [key: string]: IntCustomType;
    }
    export interface IntCustomTypeMember extends UnionMember {
      "int": IntCustomType;
    }
    export function unpack(union: Union) {
      return {
        intCustomType: union["int"] as IntCustomType
      };
    }
  }
}
