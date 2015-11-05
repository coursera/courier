import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveDefaultNone: JSONSerializable, DataTreeSerializable {
    
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
    
    public static func readJSON(json: JSON) -> WithOptionalPrimitiveDefaultNone {
        return WithOptionalPrimitiveDefaultNone(
            intWithDefault: json["intWithDefault"].int,
            longWithDefault: json["longWithDefault"].int,
            floatWithDefault: json["floatWithDefault"].float,
            doubleWithDefault: json["doubleWithDefault"].double,
            booleanWithDefault: json["booleanWithDefault"].bool,
            stringWithDefault: json["stringWithDefault"].string,
            bytesWithDefault: json["bytesWithDefault"].string,
            enumWithDefault: json["enumWithDefault"].string.map { Fruits.read($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithOptionalPrimitiveDefaultNone {
        return readJSON(JSON(data))
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
