//
//  CalendarEvent.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//

import UIKit
import ModelParsers

struct CalendarEventResponse: Decodable {
    @LossyArray var events: [CalendarEvent]
}

struct CalendarEvent: Decodable {
    @SafeDecode var date: String
    @LossyArray var taks: [String]
}
