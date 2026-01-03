//
//  CalendarService.swift
//  Sporthor
//
//  Created by derTurke on 26.06.2025.
//

import NetworkKit

enum CalendarService {
    case getCalendar(_ date: String)
    case getTaskType
    case addTaskType(_ request: [String: Any])
    case addTask(_ request: [String: Any])
    case updateTask(_ request: [String: Any])
    case getCalendarDetail(_ date: String)
    case getDrafts
    case rpeSurvey(_ request: [String: Any])
}

extension CalendarService: NetworkService {
    var path: String {
        switch self {
        case .getCalendar(let date):
            return "/api/Calendar/GetCalendar?date=" + date
        case .getTaskType:
            return "/api/Calendar/GetTaskTypes"
        case .addTaskType:
            return "/api/Calendar/AddTaskType"
        case .addTask:
            return "/api/Calendar/AddTask"
        case .getCalendarDetail(let date):
            return "/api/Calendar/GetCalendarDetail?date=\(date)"
        case .getDrafts:
            return "/api/Calendar/GetDrafts"
        case .updateTask:
            return "/api/Calendar/UpdateTask"
        case .rpeSurvey:
            return "/api/Calendar/RPESurvey"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        switch self {
        case .getCalendar,
                .getTaskType,
                .getCalendarDetail,
                .getDrafts:
            return .GET
        case .addTaskType,
                .addTask,
                .updateTask,
                .rpeSurvey:
            return .POST
        }
    }
    
    var parameters: [String : Any]? {
        switch self {
        case .getCalendar,
                .getTaskType,
                .getCalendarDetail,
                .getDrafts:
            return nil
        case .addTaskType(let request),
                .addTask(let request),
                .updateTask(let request),
                .rpeSurvey(let request):
            return request
        }
    }
    
    var token: String? {
        switch self {
        case .getCalendar,
                .getTaskType,
                .addTaskType,
                .addTask,
                .getCalendarDetail,
                .getDrafts,
                .updateTask,
                .rpeSurvey:
            return ApplicationContext.shared.authToken
        }
    }
}
