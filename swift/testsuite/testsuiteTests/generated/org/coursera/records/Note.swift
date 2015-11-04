import Foundation
import SwiftyJSON

public struct Note: JSONSerializable, Equatable {
    
    public let text: String?
    
    public init(
        text: String?
    ) {
        self.text = text
    }
    
    public static func read(json: JSON) -> Note {
        return Note(
            text: json["text"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let text = self.text {
            json["text"] = JSON(text)
        }
        return JSON(json)
    }
}
public func ==(lhs: Note, rhs: Note) -> Bool {
    return (
        (lhs.text == nil ? (rhs.text == nil) : lhs.text! == rhs.text!) &&
        true
    )
}
