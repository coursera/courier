import Foundation
import SwiftyJSON

public struct WithCaseClassCustomType: Serializable {
    
    public let short: Int?
    
    public let byte: String?
    
    public let char: String?
    
    public let int: Int?
    
    public let long: Int?
    
    public let float: Float?
    
    public let double: Double?
    
    public let string: String?
    
    public let boolean: Bool?
    
    public let boxedInt: Int?
    
    public let map: [String: Int]?
    
    public let mapKeys: [String: String]?
    
    public let array: [Int]?
    
    public let chained: String?
    
    public let chainedToCoercer: CustomInt?
    
    public init(
        short: Int?,
        byte: String?,
        char: String?,
        int: Int?,
        long: Int?,
        float: Float?,
        double: Double?,
        string: String?,
        boolean: Bool?,
        boxedInt: Int?,
        map: [String: Int]?,
        mapKeys: [String: String]?,
        array: [Int]?,
        chained: String?,
        chainedToCoercer: CustomInt?
    ) {
        self.short = short
        self.byte = byte
        self.char = char
        self.int = int
        self.long = long
        self.float = float
        self.double = double
        self.string = string
        self.boolean = boolean
        self.boxedInt = boxedInt
        self.map = map
        self.mapKeys = mapKeys
        self.array = array
        self.chained = chained
        self.chainedToCoercer = chainedToCoercer
    }
    
    public static func readJSON(json: JSON) throws -> WithCaseClassCustomType {
        return WithCaseClassCustomType(
            short: try json["short"].optional(.Number).int,
            byte: try json["byte"].optional(.String).string,
            char: try json["char"].optional(.String).string,
            int: try json["int"].optional(.Number).int,
            long: try json["long"].optional(.Number).int,
            float: try json["float"].optional(.Number).float,
            double: try json["double"].optional(.Number).double,
            string: try json["string"].optional(.String).string,
            boolean: try json["boolean"].optional(.Bool).bool,
            boxedInt: try json["boxedInt"].optional(.Number).int,
            map: try json["map"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.Number).intValue } },
            mapKeys: try json["mapKeys"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            array: try json["array"].optional(.Array).array.map { try $0.map { try $0.required(.Number).intValue } },
            chained: try json["chained"].optional(.String).string,
            chainedToCoercer: try json["chainedToCoercer"].optional(.Number).int.map { try CustomIntCoercer.coerceInput($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let short = self.short {
            dict["short"] = short
        }
        if let byte = self.byte {
            dict["byte"] = byte
        }
        if let char = self.char {
            dict["char"] = char
        }
        if let int = self.int {
            dict["int"] = int
        }
        if let long = self.long {
            dict["long"] = long
        }
        if let float = self.float {
            dict["float"] = float
        }
        if let double = self.double {
            dict["double"] = double
        }
        if let string = self.string {
            dict["string"] = string
        }
        if let boolean = self.boolean {
            dict["boolean"] = boolean
        }
        if let boxedInt = self.boxedInt {
            dict["boxedInt"] = boxedInt
        }
        if let map = self.map {
            dict["map"] = map
        }
        if let mapKeys = self.mapKeys {
            dict["mapKeys"] = mapKeys
        }
        if let array = self.array {
            dict["array"] = array
        }
        if let chained = self.chained {
            dict["chained"] = chained
        }
        if let chainedToCoercer = self.chainedToCoercer {
            dict["chainedToCoercer"] = CustomIntCoercer.coerceOutput(chainedToCoercer)
        }
        return dict
    }
}
