import Foundation
import SwiftyJSON

public struct WithPrimitiveTyperefs: Serializable {
    
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
    
    public static func readJSON(json: JSON) throws -> WithPrimitiveTyperefs {
        return WithPrimitiveTyperefs(
            intField: try json["intField"].optional(.Number).int,
            longField: try json["longField"].optional(.Number).int,
            floatField: try json["floatField"].optional(.Number).float,
            doubleField: try json["doubleField"].optional(.Number).double,
            booleanField: try json["booleanField"].optional(.Bool).bool,
            stringField: try json["stringField"].optional(.String).string,
            bytesField: try json["bytesField"].optional(.String).string
        )
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
