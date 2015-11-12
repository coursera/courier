import Foundation
import SwiftyJSON

public struct WithComplexTypesMap: Serializable, Equatable {
    
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
            empties: try json["empties"].optional(.Dictionary).dictionary.map {try $0.mapValues { try Empty.readJSON(try $0.required(.Dictionary).jsonValue) } },
            fruits: try json["fruits"].optional(.Dictionary).dictionary.map {try $0.mapValues { Fruits.read(try $0.required(.String).stringValue) } },
            arrays: try json["arrays"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Array).arrayValue.map { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) } } },
            maps: try json["maps"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Dictionary).dictionaryValue.mapValues { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) } } },
            unions: try json["unions"].optional(.Dictionary).dictionary.map {try $0.mapValues { try WithComplexTypesMapUnion.readJSON(try $0.required(.Dictionary).jsonValue) } },
            fixed: try json["fixed"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.String).stringValue } }
        )
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
