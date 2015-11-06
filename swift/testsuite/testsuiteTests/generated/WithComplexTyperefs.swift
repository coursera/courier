import Foundation
import SwiftyJSON

public struct WithComplexTyperefs: JSONSerializable, DataTreeSerializable {
    
    public let `enum`: Fruits?
    
    public let record: Empty?
    
    public let map: [String: Empty]?
    
    public let array: [Empty]?
    
    public let union: UnionTyperef?
    
    public init(
        `enum`: Fruits?,
        record: Empty?,
        map: [String: Empty]?,
        array: [Empty]?,
        union: UnionTyperef?
    ) {
        self.`enum` = `enum`
        self.record = record
        self.map = map
        self.array = array
        self.union = union
    }
    
    public static func readJSON(json: JSON) throws -> WithComplexTyperefs {
        return WithComplexTyperefs(
            `enum`: json["enum"].string.map { Fruits.read($0) },
            record: try json["record"].json.map { try Empty.readJSON($0) },
            map: try json["map"].dictionary.map { try $0.mapValues { try Empty.readJSON($0.jsonValue) } },
            array: try json["array"].array.map { try $0.map { try Empty.readJSON($0.jsonValue) } },
            union: try json["union"].json.map { try UnionTyperef.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithComplexTyperefs {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `enum` = self.`enum` {
            dict["enum"] = `enum`.write()
        }
        if let record = self.record {
            dict["record"] = record.writeData()
        }
        if let map = self.map {
            dict["map"] = map.mapValues { $0.writeData() }
        }
        if let array = self.array {
            dict["array"] = array.map { $0.writeData() }
        }
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}
