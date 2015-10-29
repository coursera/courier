import Foundation
import SwiftyJSON

struct Note: Equatable {
    
    let text: String?
    
    init(text: String?) {
        
        self.text = text
    }
    
    static func read(json: JSON) -> Note {
        return Note(
        text:
        json["text"].string
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let text = self.text {
            json["text"] = JSON(text)
        }
        
        return json
    }
}
func ==(lhs: Note, rhs: Note) -> Bool {
    return (
    
    (lhs.text == nil ? (rhs.text == nil) : lhs.text! == rhs.text!) &&
    true)
}
