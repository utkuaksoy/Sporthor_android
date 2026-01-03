//
//  CalendarRPEPresenter.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import Foundation

final class CalendarRPEPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarRPEPresenterDelegate? {
        get { return self.baseView as? CalendarRPEPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarRPEInteractorProtocol {
        get { return self.baseInteractor as! CalendarRPEInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarRPERouterProtocol {
        get { return self.baseRouter as! CalendarRPERouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarRPEPresenterDelegate,
         interactor: CalendarRPEInteractorProtocol,
         router: CalendarRPERouterProtocol,
         model: GetCalendarDetailTaskModel,
         delegate: CalendarRPEDelegate?) {
        self.model = model
        self.delegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private let model: GetCalendarDetailTaskModel
    private var selectedStar: Int = 0
    var rpeModel: [RPEModel] = []
    private weak var delegate: CalendarRPEDelegate?
}

// MARK: - CalendarRPEPresenterProtocol
extension CalendarRPEPresenter: CalendarRPEPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        prepareRPEModel()
        selectedStar = model.rpePoint
        view?.prepareStar(selectedStar: selectedStar)
    }
    
    private func navigate(_ routes: CalendarRPERoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func prepareRPEModel() {
        rpeModel = [
            RPEModel(point: "1",
                     title: "Çok Hafif Aktivite",
                     subtitle: "Dinlenme hariç her şey",
                     star: ""),
            RPEModel(point: "2-3",
                     title: "Hafif Aktivite",
                     subtitle: "Saatlerce devam edebilirsiniz, nefes almak ve konuşmak kolay",
                     star: ""),
            RPEModel(point: "4-5",
                     title: "Orta Aktivite",
                     subtitle: "Uzun süre devam edebilirsiniz, konuşmak ve kısa diyaloglar kolay",
                     star: ""),
            RPEModel(point: "6-7",
                     title: "Yoğun Aktivite",
                     subtitle: "Zorlanmaya başladığınız an, nefes nefese ve belki bir cümle konuşabilirsiniz",
                     star: ""),
            RPEModel(point: "8-9",
                     title: "Çok Yoğun Aktivite",
                     subtitle: "Egzersize devam etmek veya tek kelimeden fazla konuşmak zor",
                     star: ""),
            RPEModel(point: "10",
                     title: "Maksimum Efor",
                     subtitle: "Devam etmek veya tek bir kelime konuşmak imkansız. Tamamen nefes nefese",
                     star: ""),
        ]
        view?.reloadData()
    }
    
    func didSelectStar(at index: Int) {
        selectedStar = selectedStar != index ? index : 0
        view?.prepareStar(selectedStar: selectedStar)
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 1:
            rpeSurvey()
        default:
            break
        }
    }
    
    private func rpeSurvey() {
        let request: [String: Any] = [
            "taskId": model.id,
            "rating": selectedStar
        ]
        
        Task { @MainActor in
            await interactor.rpeSurvey(request)
        }
    }
}

// MARK: - CalendarRPEInteractorDelegate
extension CalendarRPEPresenter: CalendarRPEInteractorDelegate {
    func didRPESurvey() {
        showAlert(delegate: self,
                  type: .success,
                  message: "RPE Puanınız başarıyla kaydedilmiştir.",
                  tag: 99)
    }
}

extension CalendarRPEPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        switch tag {
        case 99:
            navigate(.rpeSurvey(delegate: delegate))
        default:
            break
        }
    }
}
