import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveDefaultNone: Serializable {
    
    public let intWithDefault: Int?
    
    public let longWithDefault: Int?
    
    public let floatWithDefault: Float?
    
    public let doubleWithDefault: Double?
    
    public let booleanWithDefault: Bool?
    
    public let stringWithDefault: String?
    
    public let bytesWithDefault: String?
    
    public let enumWithDefault: Fruits?
    
    public init(
        intWithDefault: Int? = nil,
        longWithDefault: Int? = nil,
        floatWithDefault: Float? = nil,
        doubleWithDefault: Double? = nil,
        booleanWithDefault: Bool? = nil,
        stringWithDefault: String? = nil,
        bytesWithDefault: String? = nil,
        enumWithDefault: Fruits? = nil
    ) {
        self.intWithDefault = intWithDefault
        self.longWithDefault = longWithDefault
        self.floatWithDefault = floatWithDefault
        self.doubleWithDefault = doubleWithDefault
        self.booleanWithDefault = booleanWithDefault
        self.stringWithDefault = stringWithDefault
        self.bytesWithDefault = bytesWithDefault
        self.enumWithDefault = enumWithDefault
    }
    
    public static func readJSON(json: JSON) throws -> WithOptionalPrimitiveDefaultNone {
        return WithOptionalPrimitiveDefaultNone(
            intWithDefault: try json["intWithDefault"].optional(.Number).int,
            longWithDefault: try json["longWithDefault"].optional(.Number).int,
            floatWithDefault: try json["floatWithDefault"].optional(.Number).float,
            doubleWithDefault: try json["doubleWithDefault"].optional(.Number).double,
            booleanWithDefault: try json["booleanWithDefault"].optional(.Bool).bool,
            stringWithDefault: try json["stringWithDefault"].optional(.String).string,
            bytesWithDefault: try json["bytesWithDefault"].optional(.String).string,
            enumWithDefault: try json["enumWithDefault"].optional(.String).string.map {Fruits.read($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let intWithDefault = self.intWithDefault {
            dict["intWithDefault"] = intWithDefault
        }
        if let longWithDefault = self.longWithDefault {
            dict["longWithDefault"] = longWithDefault
        }
        if let floatWithDefault = self.floatWithDefault {
            dict["floatWithDefault"] = floatWithDefault
        }
        if let doubleWithDefault = self.doubleWithDefault {
            dict["doubleWithDefault"] = doubleWithDefault
        }
        if let booleanWithDefault = self.booleanWithDefault {
            dict["booleanWithDefault"] = booleanWithDefault
        }
        if let stringWithDefault = self.stringWithDefault {
            dict["stringWithDefault"] = stringWithDefault
        }
        if let bytesWithDefault = self.bytesWithDefault {
            dict["bytesWithDefault"] = bytesWithDefault
        }
        if let enumWithDefault = self.enumWithDefault {
            dict["enumWithDefault"] = enumWithDefault.write()
        }
        return dict
    }
}
