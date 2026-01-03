//
//  AddPersonAndTechnicalStaffTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 27.10.2025.
//
//

import Foundation

final class AddPersonAndTechnicalStaffTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate? {
        get { return self.baseView as? AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AddPersonAndTechnicalStaffTrainingGroupRouterProtocol {
        get { return self.baseRouter as! AddPersonAndTechnicalStaffTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AddPersonAndTechnicalStaffTrainingGroupPresenterDelegate,
         interactor: AddPersonAndTechnicalStaffTrainingGroupInteractorProtocol,
         router: AddPersonAndTechnicalStaffTrainingGroupRouterProtocol,
         trainingGroup: TrainingGroupResponse,
         model: GetTrainingGroupUserModel?) {
        self.trainingGroup = trainingGroup
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.model = model
    }
    
    private var trainingGroup: TrainingGroupResponse
    private var model: GetTrainingGroupUserModel?
    var cellModels: [NameValueDetailModel] = []
}

// MARK: - AddPersonAndTechnicalStaffTrainingGroupPresenterProtocol
extension AddPersonAndTechnicalStaffTrainingGroupPresenter: AddPersonAndTechnicalStaffTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Grup Üyelerini Ekle")
        view?.setupView()
        view?.prepareClubInfo(trainingGroup)
        prepareCellModels()
    }
    
    private func navigate(_ routes: AddPersonAndTechnicalStaffTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareCellModels() {
        cellModels = [
            NameValueDetailModel(
                name: "Antrenör Ekibini Ekle",
                value: "Antrenör ekibini ekle antrenman grubunu birlikte yönet",
                detail: "Antrenör Ekle"
            ),
            NameValueDetailModel(
                name: "Sporcu Ekle",
                value: "Antrenman grubuna sporcuları ekle",
                detail: "Sporcu Ekle",
            )
        ]
        view?.reloadData()
    }
    
    func didTappedCellButton(_ tag: Int) {
        guard let role = TrainingGroupPersonRole(rawValue: tag) else { return }
        navigate(.addPersonWithRole(role, trainingGroup: trainingGroup))
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 1:
            navigate(.dashboard)
        default:
            break
        }
    }
}

// MARK: - AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate
extension AddPersonAndTechnicalStaffTrainingGroupPresenter: AddPersonAndTechnicalStaffTrainingGroupInteractorDelegate {

}
