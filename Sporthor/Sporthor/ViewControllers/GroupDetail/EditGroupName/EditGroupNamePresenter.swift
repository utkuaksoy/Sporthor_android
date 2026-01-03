//
//  EditGroupNamePresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import UIKit

final class EditGroupNamePresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: EditGroupNamePresenterDelegate? {
        get { return self.baseView as? EditGroupNamePresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: EditGroupNameInteractorProtocol {
        get { return self.baseInteractor as! EditGroupNameInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: EditGroupNameRouterProtocol {
        get { return self.baseRouter as! EditGroupNameRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Private Properties

    private let groupId: String
    private var groupData: EditGroupNameResponse?
    private var groupImagePath: String?
    
    // MARK: - Initialize
    init(
        view: EditGroupNamePresenterDelegate,
        interactor: EditGroupNameInteractorProtocol,
        router: EditGroupNameRouterProtocol,
        groupId: String
    ) {
        self.groupId = groupId
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - EditGroupNamePresenterProtocol
extension EditGroupNamePresenter: EditGroupNamePresenterProtocol {
    func viewDidLoad() {
        Task {
            await interactor.fetchGroupSummary(groupId: groupId)
        }
    }
    
    func uploadImage(with image: UIImage) {
        Task {
            await interactor.uploadImage(image)
        }
    }
    
    func updateProfileImage(imagePath: String) {
        self.groupImagePath = imagePath
    }
    
    func saveButtonTapped(name: String?) {
        guard let name = name?.trimmingCharacters(in: .whitespacesAndNewlines),
              !name.isEmpty else {
            showAlert(type: .error, message: "Grup adı boş olamaz")
            return
        }
        
        Task {
            await interactor.updateGroupName(
                groupId: groupId,
                name: name,
                icon: groupImagePath
            )
        }
    }
}

// MARK: - EditGroupNameInteractorDelegate
extension EditGroupNamePresenter: EditGroupNameInteractorDelegate {
    func didFetchGroupSummary(_ response: EditGroupNameResponse) {
        groupData = response
        DispatchQueue.main.async { [weak self] in
            self?.view?.updateUI(with: response)
        }
    }
    
    func didUpdateGroupName() {
        DispatchQueue.main.async { [weak self] in
            self?.router.handleRouter(.dismiss)
        }
    }
    
    func didUploadSuccess(filePath: String) {
        let imageUrl = filePath
        groupImagePath = imageUrl
    }
    
    func didFailure(_ error: Error) {
        showAlert(type: .error, message: error.localizedDescription)
    }
}
