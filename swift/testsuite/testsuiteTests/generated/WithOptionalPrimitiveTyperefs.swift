import Foundation
import SwiftyJSON

public struct WithOptionalPrimitiveTyperefs: JSONSerializable, DataTreeSerializable {
    
    public let intField: Int?
    
    public let longField: Int?
    
    public let floatField: Float?
    
    public let doubleField: Double?
    
    public let booleanField: Bool?
    
    public let stringField: String?
    
    public let bytesField: String?
    
    public init(
        intField: Int?,
        longField: Int?,
        floatField: Float?,
        doubleField: Double?,
        booleanField: Bool?,
        stringField: String?,
        bytesField: String?
    ) {
        self.intField = intField
        self.longField = longField
        self.floatField = floatField
        self.doubleField = doubleField
        self.booleanField = booleanField
        self.stringField = stringField
        self.bytesField = bytesField
    }
    
    public static func readJSON(json: JSON) throws -> WithOptionalPrimitiveTyperefs {
        return WithOptionalPrimitiveTyperefs(
            intField: json["intField"].int,
            longField: json["longField"].int,
            floatField: json["floatField"].float,
            doubleField: json["doubleField"].double,
            booleanField: json["booleanField"].bool,
            stringField: json["stringField"].string,
            bytesField: json["bytesField"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithOptionalPrimitiveTyperefs {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let intField = self.intField {
            dict["intField"] = intField
        }
        if let longField = self.longField {
            dict["longField"] = longField
        }
        if let floatField = self.floatField {
            dict["floatField"] = floatField
        }
        if let doubleField = self.doubleField {
            dict["doubleField"] = doubleField
        }
        if let booleanField = self.booleanField {
            dict["booleanField"] = booleanField
        }
        if let stringField = self.stringField {
            dict["stringField"] = stringField
        }
        if let bytesField = self.bytesField {
            dict["bytesField"] = bytesField
        }
        return dict
    }
}
