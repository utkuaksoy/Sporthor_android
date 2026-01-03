//
//  SendClubAuthorizationLetterPresenter.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

final class SendClubAuthorizationLetterPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SendClubAuthorizationLetterPresenterDelegate? {
        get { return self.baseView as? SendClubAuthorizationLetterPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SendClubAuthorizationLetterInteractorProtocol {
        get { return self.baseInteractor as! SendClubAuthorizationLetterInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SendClubAuthorizationLetterRouterProtocol {
        get { return self.baseRouter as! SendClubAuthorizationLetterRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SendClubAuthorizationLetterPresenterDelegate,
         interactor: SendClubAuthorizationLetterInteractorProtocol,
         router: SendClubAuthorizationLetterRouterProtocol,
         sportClub: SportClub) {
        self.sportClub = sportClub
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private var selectedIndexPath: IndexPath?
    var files: [FileModel] = []
    var sportClub: SportClub
}

// MARK: - SendClubAuthorizationLetterPresenterProtocol
extension SendClubAuthorizationLetterPresenter: SendClubAuthorizationLetterPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitleAndDescriptionText(
            "Kulüp yetki belgesi gönder",
            "Seçtiğin kulüp için doğrulama yapman gerekiyor. Kulüp yetki belgelerini yükleyin"
        )
        view?.prepareUI()
        files.append(FileModel())
    }
    
    private func navigate(_ routes: SendClubAuthorizationLetterRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 0: // Send
            guard files.contains(where: { $0.filePath != nil }) else {
                showAlert(type: .warning, message: "En az 1 adet Yetki belgesi yüklemelisiniz.")
                return
            }
            
            navigate(.successSendClubAuthorizationLetter(sportClub: sportClub))
        case 1: // Skip
            didTappedSkipButton()
        default:
            break
        }
        
    }
    
    private func didTappedSkipButton() {
        let teams: [TeamItemModel] = [
            TeamItemModel(name: sportClub.clubName,
                          value: sportClub.id,
                          image: sportClub.logo)
        ]
        
        if ApplicationContext.shared.isSelectedClubOfficial && ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup(teams: teams))
        } else if ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup(teams: teams))
        } else {
            navigate(.home)
        }
    }
    
    func didTappedAddNewDocument() {
        files.append(FileModel())
        view?.reloadData()
    }
    
    func didTappedUploadDocumentAt(_ indexPath: IndexPath?, isDelete: Bool) {
        self.selectedIndexPath = indexPath
        
        guard let indexPath else { return }

        if isDelete {
            guard files.indices.contains(indexPath.item) else {
                showAlert(type: .warning,
                          message: "Silinecek dosya bulunamadı.")
                return
            }
            
            if let url = files[indexPath.item].url {
                do {
                    try FileManager.default.removeItem(at: url)
                } catch {
                    showAlert(type: .error,
                              message: "Dosya silinirken bir hata oluştu. Lütfen tekrar deneyiniz.")
                    return
                }
            }
            
            if let filePath = files[indexPath.item].filePath {
                removeSportClubFiles(fileName: filePath)
            }
            
            files.remove(at: indexPath.item)
            
            if files.isEmpty {
                files.append(FileModel())
            }
            
            view?.reloadData()
        } else {
            view?.openDocumentPicker()
        }
    }

    
    func copyFile(_ url: URL) {
        var shouldStopAccessing = false
        if url.startAccessingSecurityScopedResource() {
            shouldStopAccessing = true
        }
        
        defer {
            if shouldStopAccessing {
                url.stopAccessingSecurityScopedResource()
            }
        }
        
        do {
            let resources = try url.resourceValues(forKeys: [.fileSizeKey, .typeIdentifierKey])
            let fileSize = resources.fileSize ?? 0
            
            guard fileSize <= 50_000_000 else {
                showAlert(type: .error, message: "Dosya boyutu çok büyük. Maximum 50MB yükleyebilirsiniz.")
                return
            }
            
            let cachesDirectory = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first!
            let uniqueFileName = "\(UUID().uuidString)_\(url.lastPathComponent)"
            let destinationURL = cachesDirectory.appendingPathComponent(uniqueFileName)
            
            if FileManager.default.fileExists(atPath: destinationURL.path) {
                try FileManager.default.removeItem(at: destinationURL)
            }
            try FileManager.default.copyItem(at: url, to: destinationURL)
            
            let file = FileModel(name: url.lastPathComponent, url: destinationURL)
            
            if let index = selectedIndexPath?.item, files.indices.contains(index) {
                files[index] = file
            } else {
                files.append(file)
            }
            
            view?.reloadData()
            // Her eklemede fileUpload çağrılır
            fileUpload(url: destinationURL)
        } catch {
            showAlert(type: .error, message: "Dosya işlenirken bir hata oluştu")
        }
    }
    
    private func fileUpload(url: URL) {
        Task { @MainActor in
            await interactor.uploadFile(url: url)
        }
    }
    
    
    private func updateSportClubFiles(fileName: String) {
        let request: [String: Any] = ["file": fileName,
                                      "clubId": sportClub.id]
        Task { @MainActor in
            await interactor.updateSportClubFiles(request)
        }
    }
    
    private func removeSportClubFiles(fileName: String) {
        let request: [String: Any] = ["file": fileName,
                                      "clubId": sportClub.id]
        Task { @MainActor in
            await interactor.removeSportClubFiles(request)
        }
    }
    
    func openTermsOfUse() {
        navigate(.webView(title: "Kullanım Şartları",
                          url: "https://accounts.sporthor.com/Agreement/TermsofUse"))
    }
    
    func openPrivacyPolicy() {
        navigate(.webView(title: "Gizlilik Sözlşemesi",
                          url: "https://accounts.sporthor.com/Agreement/Privacy"))
    }
}

// MARK: - SendClubAuthorizationLetterInteractorDelegate
extension SendClubAuthorizationLetterPresenter: SendClubAuthorizationLetterInteractorDelegate {
    func didSportClub(_ sportClub: SportClub?) {
        guard let sportClub else { return }
        self.sportClub = sportClub
    }
    
    func didUploadFile(_ filePath: String) {
        guard let index = selectedIndexPath?.item,
              files.indices.contains(index) else { return }
        files[index].filePath = filePath
        updateSportClubFiles(fileName: filePath)
    }
}
