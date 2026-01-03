//
//  EditPersonAndTechnicalStaffTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 31.10.2025.
//
//

import Foundation

final class EditPersonAndTechnicalStaffTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate? {
        get { return self.baseView as? EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: EditPersonAndTechnicalStaffTrainingGroupRouterProtocol {
        get { return self.baseRouter as! EditPersonAndTechnicalStaffTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: EditPersonAndTechnicalStaffTrainingGroupPresenterDelegate,
         interactor: EditPersonAndTechnicalStaffTrainingGroupInteractorProtocol,
         router: EditPersonAndTechnicalStaffTrainingGroupRouterProtocol,
         model: GetTrainingGroupUserModel?,
         trainingGroup: TrainingGroupResponse?,
         viewType: EditPersonAndTechnicalStaffTrainingGroupViewType,
         isUpdateCoach: Bool) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.model = model
        self.trainingGroup = trainingGroup
        self.viewType = viewType
        self.isUpdateCoach = isUpdateCoach
    }
    
    var model: GetTrainingGroupUserModel?
    var viewType: EditPersonAndTechnicalStaffTrainingGroupViewType = .person
    private var selectedDeleteIndexPath: IndexPath?
    private var trainingGroup: TrainingGroupResponse?
    private var isUpdateCoach: Bool = false
    private var isUpdateService: Bool = false
}

// MARK: - EditPersonAndTechnicalStaffTrainingGroupPresenterProtocol
extension EditPersonAndTechnicalStaffTrainingGroupPresenter: EditPersonAndTechnicalStaffTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Antreman Grubu")
        view?.prepareNavigationBar()
        view?.prepareUI()
        view?.prepareCKImageTitleView(
            image: model?.groupImage ?? "",
            title: model?.groupName ?? "",
            rightImage: Asset.chevronRightWhiteIcon.image,
            backgroundImage: Asset.smallGradient.image
        )
        prepareAddButtonTitle()
        
        if isUpdateCoach {
            view?.hiddenTab()
        }
    }
    
    private func navigate(_ routes: EditPersonAndTechnicalStaffTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }

    func segmentedControl(didSelect index: Int) {
        viewType = EditPersonAndTechnicalStaffTrainingGroupViewType(rawValue: index) ?? .person
        prepareAddButtonTitle()
        view?.reloadData()
    }
    
    private func prepareAddButtonTitle() {
        switch viewType {
        case .person:
            view?.prepareAddButtonTitle("Yeni Sporcu Ekle")
        case .technicalStaff:
            view?.prepareAddButtonTitle("Yeni Antrenör Ekle")
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        switch viewType {
        case .technicalStaff:
            guard let technicalStaff = model?.coaches[safe: indexPath.row],
                    ApplicationContext.shared.userId != technicalStaff.id else { return }
            navigate(.updateTechnicalStaff(delegate: self,
                                           trainingGroupId: model?.groupId ?? "",
                                           userId: technicalStaff.id,
                                           image: technicalStaff.imageUrl,
                                           name: technicalStaff.name,
                                           role: technicalStaff.summary,
                                           index: indexPath.row))
        default:
            break
        }
        
    }
    
    func deletePersonTrainingGroup(_ indexPath: IndexPath) {
        if isUpdateCoach {
            guard var request = updateCoachPrepareRequest() else { return }
            selectedDeleteIndexPath = indexPath
            guard let technicalStaff = model?.coaches[safe: indexPath.row] else { return }
            request.coaches.removeAll { $0.val == technicalStaff.id }
            addTrainingGroupUser(request.dictionary() ?? [:], isDeleted: true)
        } else {
            guard var request = prepareRequest() else { return }
            selectedDeleteIndexPath = indexPath
            
            switch viewType {
            case .person:
                guard let user = model?.users[safe: indexPath.row] else { return }
                request.users.removeAll { $0 == user.id }
            case .technicalStaff:
                guard let technicalStaff = model?.coaches[safe: indexPath.row] else { return }
                request.coaches.removeAll { $0.val == technicalStaff.id }
            }
            
            addTrainingGroupUser(request.dictionary() ?? [:], isDeleted: true)
        }
    }
    
    private func prepareRequest() -> AddPersonWithRoleRequest? {
        guard let trainingGroup else { return nil }
        let selectedUsers: [String] = model?.users.compactMap {
            return $0.id
        } ?? []
        
        let selectedCoaches: [PersonWithRoleModel] = model?.coaches.compactMap {
            return PersonWithRoleModel(name: $0.name, val: $0.id, val2: $0.summary)
        } ?? []
        
        return AddPersonWithRoleRequest(
            groupId: trainingGroup.trainingGroupId,
            users: selectedUsers,
            coaches: selectedCoaches
        )
    }
    
    private func updateCoachPrepareRequest() -> AddCoachesWithRoleRequest? {
        guard let trainingGroup else { return nil }
        
        let selectedCoaches: [PersonWithRoleModel] = model?.coaches.compactMap {
            return PersonWithRoleModel(name: $0.name, val: $0.id, val2: $0.summary)
        } ?? []
        
        return AddCoachesWithRoleRequest(
            trainingGroupId: trainingGroup.trainingGroupId,
            coaches: selectedCoaches
        )
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 0:
            guard let trainingGroup else { return }
            navigate(.addPersonTrainingGroup(role: viewType == .person ? .person : .technicalStaff,
                                             trainingGroup: trainingGroup,
                                             isEdit: true,
                                             users: model?.users ?? [],
                                             coaches: model?.coaches ?? [],
                                             delegate: self,
                                             isUpdateCoach: isUpdateCoach))
        case 1:
            navigate(.dashboard)
        default:
            break
        }
    }
    
    private func removeTrainingGroupUser() {
        guard let selectedDeleteIndexPath else { return }
        switch viewType {
        case .person:
            guard let _ = model?.users[safe: selectedDeleteIndexPath.row] else { return }
            model?.users.remove(at: selectedDeleteIndexPath.row)
        case .technicalStaff:
            guard let _ = model?.coaches[safe: selectedDeleteIndexPath.row] else { return }
            model?.coaches.remove(at: selectedDeleteIndexPath.row)
        }
    }
    
    private func addTrainingGroupUser(_ request: [String: Any],
                                      isDeleted: Bool = false) {
        Task { @MainActor in
            if isUpdateCoach {
                await interactor.updateCoach(request, isDelete: isDeleted)
            } else {
                await interactor.addTrainingGroupUser(request, isDelete: isDeleted)
            }
            
        }
    }
    
    func didTappedCKImageTitleView(_ tag: Int) {
        if isUpdateCoach {
            navigate(.myTeamCoaches)
        } else {
            navigate(.backToListTrainingViewController)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            if isUpdateCoach, isUpdateService {
                navigate(.myTeamCoaches)
            } else {
                navigate(.back)
            }
        default:
            break
        }
    }
    
    private func notificationTrigger() {
        if isUpdateCoach {
            isUpdateService = true
            NotificationCenter.default.post(name: .didAddCoachAtTeam, object: nil)
        }
    }
}

// MARK: - EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate
extension EditPersonAndTechnicalStaffTrainingGroupPresenter: EditPersonAndTechnicalStaffTrainingGroupInteractorDelegate {
    func didAddTrainingGroupUser(isDelete: Bool) {
        if isDelete {
            removeTrainingGroupUser()
        }
        
        notificationTrigger()
        view?.reloadData()
    }
}

extension EditPersonAndTechnicalStaffTrainingGroupPresenter: AddTechnicalStaffDelegate {
    func changeRoleAddTechnicalStaff(at index: Int, to role: String) {
        guard let _ = model?.coaches[safe: index] else { return }
        model?.coaches[index].summary = role
        notificationTrigger()
        addTrainingGroupUser(isUpdateCoach ? updateCoachPrepareRequest()?.dictionary() ?? [:] : prepareRequest()?.dictionary() ?? [:])
    }
}

extension EditPersonAndTechnicalStaffTrainingGroupPresenter: AddPersonWithRoleTrainingGroupDelegate {
    func didAddPersons(type: TrainingGroupPersonRole, users: [GetTrainingGroupUserModelUser]) {
        switch type {
        case .person:
            model?.users = users
        case .technicalStaff:
            model?.coaches = users
        }
        
        notificationTrigger()
        view?.reloadData()
    }
}
