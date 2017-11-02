export interface WithPrimitivesUnion {
  
  union : WithPrimitivesUnion.Union;
}
export module WithPrimitivesUnion {
  
  export type Union = Union.IntMember | Union.LongMember | Union.FloatMember | Union.DoubleMember | Union.BooleanMember | Union.StringMember | Union.BytesMember;
  export module Union {
    export interface UnionMember {
      [key: string]: number | number | number | number | boolean | string | string;
    }
    export interface IntMember extends UnionMember {
      "int": number;
    }
    export interface LongMember extends UnionMember {
      "long": number;
    }
    export interface FloatMember extends UnionMember {
      "float": number;
    }
    export interface DoubleMember extends UnionMember {
      "double": number;
    }
    export interface BooleanMember extends UnionMember {
      "boolean": boolean;
    }
    export interface StringMember extends UnionMember {
      "string": string;
    }
    export interface BytesMember extends UnionMember {
      "bytes": string;
    }
    export function unpack(union: Union) {
      return {
        int: union["int"] as number,
        long: union["long"] as number,
        float: union["float"] as number,
        double: union["double"] as number,
        boolean$: union["boolean"] as boolean,
        string$: union["string"] as string,
        bytes: union["bytes"] as string
      };
    }
  }
}
