import Foundation
import SwiftyJSON

struct WithOptionalPrimitives: JSONSerializable {
    
    let intField: Int?
    
    let longField: Int?
    
    let floatField: Float?
    
    let doubleField: Double?
    
    let booleanField: Bool?
    
    let stringField: String?
    
    let bytesField: String?
    
    init(
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
    
    static func read(json: JSON) -> WithOptionalPrimitives {
        return WithOptionalPrimitives(
            intField: json["intField"].int,
            longField: json["longField"].int,
            floatField: json["floatField"].float,
            doubleField: json["doubleField"].double,
            booleanField: json["booleanField"].bool,
            stringField: json["stringField"].string,
            bytesField: json["bytesField"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let intField = self.intField {
            json["intField"] = JSON(intField)
        }
        if let longField = self.longField {
            json["longField"] = JSON(longField)
        }
        if let floatField = self.floatField {
            json["floatField"] = JSON(floatField)
        }
        if let doubleField = self.doubleField {
            json["doubleField"] = JSON(doubleField)
        }
        if let booleanField = self.booleanField {
            json["booleanField"] = JSON(booleanField)
        }
        if let stringField = self.stringField {
            json["stringField"] = JSON(stringField)
        }
        if let bytesField = self.bytesField {
            json["bytesField"] = JSON(bytesField)
        }
        return JSON(json)
    }
}
