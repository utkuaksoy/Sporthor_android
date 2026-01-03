//
//  NotificationPresenter.swift
//  Sporthor
//
//  Created by derTurke on 14.06.2025.
//
//

import Foundation

final class NotificationPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: NotificationPresenterDelegate? {
        get { return self.baseView as? NotificationPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: NotificationInteractorProtocol {
        get { return self.baseInteractor as! NotificationInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: NotificationRouterProtocol {
        get { return self.baseRouter as! NotificationRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: NotificationPresenterDelegate,
         interactor: NotificationInteractorProtocol,
         router: NotificationRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var notifications: [NotificationModel] = []
    var headerTabs: [NotificationHeaderModel] = [
        NotificationHeaderModel(id: 0, title: "Tümü", isSelected: true),
        NotificationHeaderModel(id: 1, title: "Takip İsteği", isSelected: false),
        NotificationHeaderModel(id: 2, title: "Beğeniler", isSelected: false),
        NotificationHeaderModel(id: 3, title: "Etkinlikler", isSelected: false)
    ]
}

// MARK: - NotificationPresenterProtocol
extension NotificationPresenter: NotificationPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Bildirimler")
        view?.prepareUI()
        getNotifications()
    }
    
    private func getNotifications() {
        Task { @MainActor in
            await interactor.getNotifications()
        }
    }
    
    private func navigate(_ routes: NotificationRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    func didSelectHeaderTab(model: NotificationHeaderModel) {
        for i in headerTabs.indices {
            headerTabs[i].isSelected = false
        }
        
        if let index = headerTabs.firstIndex(where: { $0.id == model.id }) {
            headerTabs[index].isSelected = true
        }
        
        view?.reloadData()
    }
    
    func openProfile(model: NotificationModel) {
        guard let data = model.data else { return }
        navigate(.openProfile(userId: data.userId, username: data.username))
    }
    
    func openChat(model: NotificationModel) {
        guard let data = model.data else { return }
        navigate(.chat(userId: model.userId,
                       displayName: data.username,
                       image: data.imageURL,
                       isGroup: false,
                       toUserId: data.toUserId))
    }
    
    func didTappedTrainingGroupRequestButton(_ model: NotificationModel, isAccepted: Bool) {
        let request: [String: Any] = [
            "groupId": model.data?.trainingGroupId ?? "",
            "isAccepted": isAccepted,
            "notificationId": model.id
        ]
        
        Task { @MainActor in
            await interactor.confirmationTrainingGroupUser(request)
        }
    }
    
    func didTappedFollowRequestButton(_ model: NotificationModel, isAccepted: Bool) {
        let request: [String: Any] = [
            "targetUserId": model.data?.userId ?? "",
            "isAccepted": isAccepted,
            "notificationId": model.id
        ]
        
        Task { @MainActor in
            await interactor.confirmationFollow(request)
        }
    }
}

// MARK: - NotificationInteractorDelegate
extension NotificationPresenter: NotificationInteractorDelegate {
    func didGetNotifications(_ notifications: [NotificationModel]) {
        self.notifications = notifications
        view?.reloadData()
    }
    
    func didConfirmationTrainingGroupUser() {
        getNotifications()
    }
}

extension NotificationPresenter {
    var filteredNotifications: [NotificationModel] {
        guard let selectedTab = headerTabs.first(where: { $0.isSelected }) else {
            return []
        }

        switch selectedTab.id {
        case 0: // Tümü - Confirm olanlar en üstte
            return notifications.sorted { lhs, rhs in
                // .confirm olanları önce sırala
                let lhsIsConfirm = lhs.notificationType == .confirm ? 1 : 0
                let rhsIsConfirm = rhs.notificationType == .confirm ? 1 : 0
                return lhsIsConfirm > rhsIsConfirm
            }

        case 1: // Takip İsteği
            return notifications.filter {
                $0.notificationType == .confirm
            }

        case 2: // Beğeniler
            return notifications.filter {
                $0.pushMessageType == .like
            }

        case 3: // Etkinlikler
            return notifications.filter {
                $0.pushMessageType == .newTask
            }

        default:
            return []
        }
    }
}

