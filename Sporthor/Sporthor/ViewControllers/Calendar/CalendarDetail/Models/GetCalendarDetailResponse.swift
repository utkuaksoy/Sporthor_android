//
//  GetCalendarDetailResponse.swift
//  Sporthor
//
//  Created by derTurke on 29.06.2025.
//

import Foundation
import ModelParsers

struct GetCalendarDetailResponse: Decodable {
    @LossyArray var tasks: [GetCalendarDetailTaskModel]
}

struct GetCalendarDetailTaskModel: Decodable {
    @SafeDecode var id: String
    @SafeDecode var title: String
    @SafeDecode var hour: String
    @SafeDecode var allDay: Bool
    var location: GetCalendarDetailLocation?
    @SafeDecode var isRecurring: Bool
    @SafeDecode var startDate: String
    @SafeDecode var endDate: String
    @SafeDecode var recurrence: Int
    var taskType: EventTypeModel?
    @SafeDecode var description: String
    var trainingGroup: TeamItemModel?
    @LossyArray var users: [GetTrainingGroupUserModelUser]
    @SafeDecode var isOwn: Bool
    @SafeDecode var rpePoint: Int
}

struct GetCalendarDetailLocation: Decodable {
    @SafeDecode var title: String
    @SafeDecode var address: String
    @SafeDecode var lat: Double
    @SafeDecode var lng: Double
}

