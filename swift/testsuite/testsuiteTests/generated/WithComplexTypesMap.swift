import Foundation
import SwiftyJSON

public struct WithComplexTypesMap: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let empties: [String: Empty]?
    
    public let fruits: [String: Fruits]?
    
    public let arrays: [String: [Simple]]?
    
    public let maps: [String: [String: Simple]]?
    
    public let unions: [String: WithComplexTypesMapUnion]?
    
    public let fixed: [String: String]?
    
    public init(
        empties: [String: Empty]?,
        fruits: [String: Fruits]?,
        arrays: [String: [Simple]]?,
        maps: [String: [String: Simple]]?,
        unions: [String: WithComplexTypesMapUnion]?,
        fixed: [String: String]?
    ) {
        self.empties = empties
        self.fruits = fruits
        self.arrays = arrays
        self.maps = maps
        self.unions = unions
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) throws -> WithComplexTypesMap {
        return WithComplexTypesMap(
            empties: try json["empties"].dictionary.map { try $0.mapValues { try Empty.readJSON($0.jsonValue) } },
            fruits: json["fruits"].dictionary.map { $0.mapValues { Fruits.read($0.stringValue) } },
            arrays: try json["arrays"].dictionary.map { try $0.mapValues { try $0.arrayValue.map { try Simple.readJSON($0.jsonValue) } } },
            maps: try json["maps"].dictionary.map { try $0.mapValues { try $0.dictionaryValue.mapValues { try Simple.readJSON($0.jsonValue) } } },
            unions: try json["unions"].dictionary.map { try $0.mapValues { try WithComplexTypesMapUnion.readJSON($0.jsonValue) } },
            fixed: json["fixed"].dictionary.map { $0.mapValues { $0.stringValue } }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithComplexTypesMap {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let empties = self.empties {
            dict["empties"] = empties.mapValues { $0.writeData() }
        }
        if let fruits = self.fruits {
            dict["fruits"] = fruits.mapValues { $0.write() }
        }
        if let arrays = self.arrays {
            dict["arrays"] = arrays.mapValues { $0.map { $0.writeData() } }
        }
        if let maps = self.maps {
            dict["maps"] = maps.mapValues { $0.mapValues { $0.writeData() } }
        }
        if let unions = self.unions {
            dict["unions"] = unions.mapValues { $0.writeData() }
        }
        if let fixed = self.fixed {
            dict["fixed"] = fixed.mapValues { $0 }
        }
        return dict
    }
}
public func ==(lhs: WithComplexTypesMap, rhs: WithComplexTypesMap) -> Bool {
    return (
        (lhs.empties == nil ? (rhs.empties == nil) : lhs.empties! == rhs.empties!) &&
        (lhs.fruits == nil ? (rhs.fruits == nil) : lhs.fruits! == rhs.fruits!) &&
        (lhs.arrays == nil ? (rhs.arrays == nil) : lhs.arrays! == rhs.arrays!) &&
        (lhs.maps == nil ? (rhs.maps == nil) : lhs.maps! == rhs.maps!) &&
        (lhs.unions == nil ? (rhs.unions == nil) : lhs.unions! == rhs.unions!) &&
        (lhs.fixed == nil ? (rhs.fixed == nil) : lhs.fixed! == rhs.fixed!) &&
        true
    )
}
