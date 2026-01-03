//
//  CreateClubPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import CommonKit
import MapKit

final class CreateClubPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreateClubPresenterDelegate? {
        get { return self.baseView as? CreateClubPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreateClubInteractorProtocol {
        get { return self.baseInteractor as! CreateClubInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreateClubRouterProtocol {
        get { return self.baseRouter as! CreateClubRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CreateClubPresenterDelegate,
         interactor: CreateClubInteractorProtocol,
         router: CreateClubRouterProtocol,
         model: SportClub?,
         isLogin: Bool = false) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.sportClub = model
        self.isLogin = isLogin
    }
    var clubName: String = ""
    var location: String = ""
    var fullAddress: String = ""
    var foundationDate: String?
    private var placemark: CLPlacemark?
    private var clubLogoFilePath: String = ""
    private var clubLogoImage: UIImage = UIImage()
    private var sportClub: SportClub?
    private var isEdit: Bool = false
    private var isLogin: Bool = false
    private var branches = ApplicationContext.shared.getConfiguration.branches
    private var clubId: String = ""
    var selectedBranch: NameValueDetailModel?
}

// MARK: - CreateClubPresenterProtocol
extension CreateClubPresenter: CreateClubPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        
        if let sportClub {
            isEdit = true
            view?.didSetTitleAndDescriptionText("Kulübünü Düzenle",
                                                "Kulüp bilgilerini girerek düzenle")
            view?.setContinueButtonTitle("Kulübü Düzenle")
            clubId = sportClub.clubId
            clubName = sportClub.clubName
            location = sportClub.county
            fullAddress = sportClub.address
            foundationDate = sportClub.foundationYear
            clubLogoFilePath = sportClub.logo
            view?.setEditClubImage(sportClub.logo)
            selectedBranch = branches?.first { return $0.value == sportClub.branch?.val }
            view?.reloadData()
        } else {
            view?.didSetTitleAndDescriptionText("Kulübünü Oluştur",
                                                "Kulüp bilgilerini girerek oluştur")
            view?.setContinueButtonTitle("Kulübü Oluştur")
        }
    }
    
    func viewWillAppear() {
        view?.setNavigationBarHidden(!isLogin)
    }
    
    func viewWillDisappear() {
        view?.setNavigationBarHidden(false)
    }
    
    private func navigate(_ routes: CreateClubRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func presentImagePickerOptions() {
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let cameraAction = UIAlertAction(title: "Kamera", style: .default) { [weak self] _ in
            guard let self else { return }
            self.checkCameraPermission()
        }
        
        let galleryAction = UIAlertAction(title: "Galeri", style: .default) { [weak self] _ in
            guard let self else { return }
            self.checkPhotoLibraryPermission()
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        self.navigate(.showAlertController(alertController))
    }
    
    private func checkCameraPermission() {
        BaseHelper.shared.checkCameraPermission { [weak self] authorized in
            guard let self else { return }
            if authorized {
                DispatchQueue.main.async {
                    self.view?.presentCamera(sourceType: .camera)
                }
            } else {
                self.navigate(.showAlertController(BaseHelper.shared.showPermissionAlert(for: .camera)))
            }
        }
    }
    
    private func checkPhotoLibraryPermission() {
        BaseHelper.shared.checkPhotoLibraryPermission { [weak self] authorized in
            guard let self else { return }
            if authorized {
                DispatchQueue.main.async {
                    self.view?.presentCamera(sourceType: .photoLibrary)
                }
            } else {
                self.navigate(.showAlertController(BaseHelper.shared.showPermissionAlert(for: .gallery)))
            }
        }
    }
    
    func presentCamera(_ imagePickerViewController: UIImagePickerController) {
        navigate(.camera(imagePickerController: imagePickerViewController))
    }
    
    func didTapSelection(_ tag: Int) {
        switch tag {
        case 1:
            navigate(.location(delegate: self))
        case 2:
            let selectionModel = branches?.compactMap {
                SelectionModel(
                    id: $0.value,
                    value: $0.name,
                    image: $0.detail,
                    isSelected: selectedBranch?.value == $0.value
                )
            }
            
            guard let selectionModel, !selectionModel.isEmpty else { return }
            navigate(.selection(title: "Branş Ekle", model: selectionModel, delegate: self, isSingleSelection: true))
        default:
            break
        }
    }
    
    func textFieldDidEndEditing(_ text: String, tag: Int) {
        switch tag {
        case 0:
            clubName = text
        case 1:
            fullAddress = text
        case 2:
            foundationDate = text
        default:
            break
        }
    }
    
    func didTappedContinueButton() {
        if clubName.isEmpty {
            showAlert(type: .warning, message: "Lütfen kulüp adı giriniz.")
            return
        }
        
        if clubLogoFilePath.isEmpty {
            showAlert(type: .warning, message: "Lütfen kulüp logosu seçiniz.")
            return
        }
        
        if location.isEmpty {
            showAlert(type: .warning, message: "Lütfen adres seçiniz.")
        }
        
        if fullAddress.isEmpty {
            showAlert(type: .warning, message: "Lütfen açık adres giriniz.")
            return
        }
        
        if let selectedBranch,
           let branchId = selectedBranch.value,
           branchId.isEmpty {
            showAlert(type: .warning, message: "Lütfen branş seçiniz.")
            return
        }
        
        
        var request: [String: Any] = ["clubName": clubName,
                                      "logo": clubLogoFilePath,
                                      "city": placemark?.administrativeArea ?? sportClub?.city ?? "",
                                      "county": placemark?.locality ?? sportClub?.county ?? "",
                                      "address": fullAddress,
                                      "foundationYear": foundationDate ?? "",
                                      "branchId": selectedBranch?.value ?? ""]
        if isEdit {
            request["id"] = clubId
        }
        Task { @MainActor in
            await interactor.addSportClub(request, isEdit: isEdit)
        }
    }
    
    func uploadImage(_ image: UIImage) {
        self.clubLogoImage = image
        Task { @MainActor in
            if clubLogoFilePath.isEmpty {
                await interactor.uploadImage(image)
            } else {
                await interactor.deleteImage(filePath: clubLogoFilePath)
            }
        }
    }
}

// MARK: - CreateClubInteractorDelegate
extension CreateClubPresenter: CreateClubInteractorDelegate {
    func didUploadImage(filePath: String) {
        self.clubLogoFilePath = filePath
    }
    
    func didDeleteImage() {
        self.clubLogoFilePath = ""
        Task { @MainActor in
            await interactor.uploadImage(clubLogoImage)
        }
    }
    
    func didAddSportClub(_ sportClub: SportClub?) {
        guard let sportClub else { return }
        self.sportClub = sportClub
        var infoTitle: String = ""
        var infoDescription: String = ""
        
        if isEdit {
            infoTitle = "Tebrikler! Kulübünü Güncelledin."
            infoDescription = "Şimdi antrenörlerini ve teknik ekibini güncelle ve güçlü bir topluluk oluştur.\n\nBirlikte başarıya ulaşmak için takım ruhunu yakala! 🚀"
        } else {
            infoTitle = "Tebrikler! Kulübünü Oluşturdun."
            infoDescription = "Şimdi antrenörlerini ve teknik ekibini davet et ve güçlü bir topluluk oluşturmaya başla.\n\nBirlikte başarıya ulaşmak için takım ruhunu yakala! 🚀"
        }
        
        navigate(.successCreateClub(
            delegate: self,
            sportClub: sportClub,
            infoTitle: infoTitle,
            infoDescription: infoDescription)
        )
    }
}

// MARK: - LocationDelegate
extension CreateClubPresenter: LocationDelegate {
    func didSelectPlacemark(placemark: CLPlacemark) {
        self.placemark = placemark
        self.location = placemark.areasOfInterest?.first ?? placemark.name ?? ""
        self.fullAddress = [
            placemark.name,
            placemark.thoroughfare,
            placemark.subThoroughfare,
            placemark.locality,
            placemark.administrativeArea,
            placemark.postalCode,
            placemark.country
        ]
        .compactMap { $0 }
        .joined(separator: ", ")
        view?.reloadData()
    }
}

// MARK: - SuccessCreateClubDelegate
extension CreateClubPresenter: SuccessCreateClubDelegate {
    func didTappedSendAuthorizationLetter() {
        guard let sportClub else { return }
        navigate(.sendClubAuthorizationLetter(sportClub: sportClub))
    }
    
    func didTappedSkipButton() {
        if ApplicationContext.shared.isSelectedClubOfficial && ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup)
        } else if ApplicationContext.shared.isSelectedCoach {
            navigate(.createTrainingGroup)
        } else {
            navigate(.home)
        }
    }
}

extension CreateClubPresenter: ProfileEditAddBranchDelegate {
    func didSelectItems(_ items: [SelectionModel]) {
        guard let item = items.first(where: { $0.isSelected }) else { return }
        selectedBranch = NameValueDetailModel(name: item.value, value: item.id, detail: item.image)
        view?.reloadData()
    }
}
