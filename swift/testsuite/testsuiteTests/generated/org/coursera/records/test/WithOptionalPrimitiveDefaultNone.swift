import Foundation
import SwiftyJSON

struct WithOptionalPrimitiveDefaultNone {
    
    let intWithDefault: Int?
    
    let longWithDefault: Int?
    
    let floatWithDefault: Float?
    
    let doubleWithDefault: Double?
    
    let booleanWithDefault: Bool?
    
    let stringWithDefault: String?
    
    let bytesWithDefault: String?
    
    let enumWithDefault: Fruits?
    
    init(intWithDefault: Int? = nil, longWithDefault: Int? = nil, floatWithDefault: Float? = nil, doubleWithDefault: Double? = nil, booleanWithDefault: Bool? = nil, stringWithDefault: String? = nil, bytesWithDefault: String? = nil, enumWithDefault: Fruits? = nil) {
        
        self.intWithDefault = intWithDefault
        
        self.longWithDefault = longWithDefault
        
        self.floatWithDefault = floatWithDefault
        
        self.doubleWithDefault = doubleWithDefault
        
        self.booleanWithDefault = booleanWithDefault
        
        self.stringWithDefault = stringWithDefault
        
        self.bytesWithDefault = bytesWithDefault
        
        self.enumWithDefault = enumWithDefault
    }
    
    static func read(json: JSON) -> WithOptionalPrimitiveDefaultNone {
        return WithOptionalPrimitiveDefaultNone(
        intWithDefault:
        json["intWithDefault"].int,
        longWithDefault:
        json["longWithDefault"].int,
        floatWithDefault:
        json["floatWithDefault"].float,
        doubleWithDefault:
        json["doubleWithDefault"].double,
        booleanWithDefault:
        json["booleanWithDefault"].bool,
        stringWithDefault:
        json["stringWithDefault"].string,
        bytesWithDefault:
        json["bytesWithDefault"].string,
        enumWithDefault:
        json["enumWithDefault"].string.map { Fruits.read($0) }
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let intWithDefault = self.intWithDefault {
            json["intWithDefault"] = JSON(intWithDefault)
        }
        
        if let longWithDefault = self.longWithDefault {
            json["longWithDefault"] = JSON(longWithDefault)
        }
        
        if let floatWithDefault = self.floatWithDefault {
            json["floatWithDefault"] = JSON(floatWithDefault)
        }
        
        if let doubleWithDefault = self.doubleWithDefault {
            json["doubleWithDefault"] = JSON(doubleWithDefault)
        }
        
        if let booleanWithDefault = self.booleanWithDefault {
            json["booleanWithDefault"] = JSON(booleanWithDefault)
        }
        
        if let stringWithDefault = self.stringWithDefault {
            json["stringWithDefault"] = JSON(stringWithDefault)
        }
        
        if let bytesWithDefault = self.bytesWithDefault {
            json["bytesWithDefault"] = JSON(bytesWithDefault)
        }
        
        if let enumWithDefault = self.enumWithDefault {
            json["enumWithDefault"] = JSON(enumWithDefault.write())
        }
        
        return json
    }
}
