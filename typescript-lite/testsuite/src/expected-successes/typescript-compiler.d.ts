declare module "typescript-compiler" { // hilariously, no typings exist for the typescript-compiler package so we have to make our own!
  export function compileString(input:string, tscArgs?:any, options?:any, onError?:(diag: any) => any): string;
  export function compileStrings(input:any, tscArgs?:any, options?:any, onError?:(diag: any) => any): string;
}

declare module jasmine {
  export interface Matchers {
    toCompile(errMsg: string): boolean;
  }
}
