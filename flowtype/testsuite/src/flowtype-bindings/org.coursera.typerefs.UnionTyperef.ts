export type UnionTyperef = UnionTyperef.StringMember | UnionTyperef.IntMember;
export module UnionTyperef {
  export interface UnionTyperefMember {
    [key: string]: string | number;
  }
  export interface StringMember extends UnionTyperefMember {
    "string": string;
  }
  export interface IntMember extends UnionTyperefMember {
    "int": number;
  }
  export function unpack(union: UnionTyperef) {
    return {
      string$: union["string"] as string,
      int: union["int"] as number
    };
  }
}
