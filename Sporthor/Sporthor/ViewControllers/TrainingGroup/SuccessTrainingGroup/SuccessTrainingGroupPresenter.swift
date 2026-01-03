//
//  SuccessTrainingGroupPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessTrainingGroupPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SuccessTrainingGroupPresenterDelegate? {
        get { return self.baseView as? SuccessTrainingGroupPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SuccessTrainingGroupInteractorProtocol {
        get { return self.baseInteractor as! SuccessTrainingGroupInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SuccessTrainingGroupRouterProtocol {
        get { return self.baseRouter as! SuccessTrainingGroupRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SuccessTrainingGroupPresenterDelegate,
         interactor: SuccessTrainingGroupInteractorProtocol,
         router: SuccessTrainingGroupRouterProtocol,
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
}

// MARK: - SuccessTrainingGroupPresenterProtocol
extension SuccessTrainingGroupPresenter: SuccessTrainingGroupPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.prepareTeam(image: trainingGroup.logo,
                          name: trainingGroup.teamName,
                          groupName: trainingGroup.groupName)
        
        var infoTitle: String = "Tebrikler! Grubunu oluşturdun"
        var infoDescription: String = "Antreman grubun hazır! Şimdi oyuncularını ve antrenör ekibini davet et ve güçlü bir topluluk oluşturmaya başla.\n\nBirlikte başarıya ulaşmak için takım ruhunu yakala! 🚀"
        
        if let model {
            infoTitle = "Tebrikler! Grubunu güncelledin"
            infoDescription =  "Antreman grubun güncel! Şimdi oyuncularını ve antrenör ekibini güncelle ve güçlü bir topluluk oluşturmaya devam et.\n\nBirlikte başarıya ulaşmak için takım ruhunu yakala! 🚀"
        }
        
        view?.prepareSuccessHeaderAndDescription(
            header: infoTitle,
            description: infoDescription
        )
    }
    
    private func navigate(_ routes: SuccessTrainingGroupRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedCKButton(_ tag: Int) {
        if let model {
            navigate(.editPersonTraining(trainingGroup: trainingGroup, model: model))
        } else {
            navigate(.addPersonTraining(trainingGroup: trainingGroup, model: model))
        }
    }
}

// MARK: - SuccessTrainingGroupInteractorDelegate
extension SuccessTrainingGroupPresenter: SuccessTrainingGroupInteractorDelegate {

}
